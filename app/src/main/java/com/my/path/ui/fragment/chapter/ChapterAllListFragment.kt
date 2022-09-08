package com.my.path.ui.fragment.chapter

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.TimePickerView
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.config.SysConfig
import com.my.path.app.eventViewModel
import com.my.path.app.ext.*
import com.my.path.app.util.TimeUtil.getDataToStringYMD
import com.my.path.app.util.TimeUtil.stringToTimestamp
import com.my.path.app.util.TimeUtil.timestampToTime
import com.my.path.data.model.bean.*
import com.my.path.databinding.FragmentChapterAllBinding
import com.my.path.viewmodel.request.chapter.RequestChapterDataViewModel
import com.my.path.viewmodel.state.chapter.ChapterListViewModel
import com.drake.brv.item.ItemExpand
import com.drake.brv.utils.*
import com.kingja.loadsir.core.LoadService
import me.guangnian.mvvm.ext.parseState
import java.util.*

/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　: h5页面
 */
class ChapterAllListFragment(val novel: Novel,val type:Int)  : BaseFragment<ChapterListViewModel, FragmentChapterAllBinding>() {
    var listChapterData:ArrayList<ChapterData> = ArrayList()
    private lateinit var loadsir: LoadService<Any>
    private val requestChapterDataViewModel: RequestChapterDataViewModel by viewModels()
    private var pvTime: TimePickerView? = null
    private var Dxresult:ArrayList<ChapterData> = ArrayList()
    private var barTouchedLastY = 0f //拖动条

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.viewmodel = mViewModel

        eventViewModel.draftBoxEvent.observeInFragment(this){
            lazyLoadData()
        }

        eventViewModel.inAddChapter.observeInFragment(this){
            inChapterAddFragment(novel.bid,listChapterData)
        }

        eventViewModel.isReverseOrder.observeInFragment(this){
            mViewModel.isReverseOrder.set(it)
            mDatabind.includeList.includeRecyclerview.recyclerView.models = setDataGro(Dxresult)
        }

        mDatabind.includeList.includeRecyclerview.recyclerView.linear().setup {
            addType<ChapterGroupModel>(R.layout.item_chapter_group)
            addType<ChapterData>(R.layout.item_chapter)

            R.id.item.onFastClick {
                when (itemViewType) {
                    R.layout.item_chapter_group -> {
                        val changeCount =
                            if (getModel<ItemExpand>().itemExpand) "折叠 ${expandOrCollapse()} 条" else "展开 ${expandOrCollapse()} 条"

//                        toast(changeCount)
                    }
                }
            }

            R.id.tvTimg.onClick {
                getModelOrNull<ChapterData>()?.let {
                    timeChoose(modelPosition,it.timing)
                }
            }

            R.id.imageViewGD.onClick{
                requireContext().showMobilePopup(findView(R.id.imageViewGD)){
                    if(it){
                        mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.getModelOrNull<ChapterData>(modelPosition)?.let {
                            requestChapterDataViewModel.authorChaptermoveup(novel.bid,it.cid,modelPosition)
                        }
                    }else{
                        mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.getModelOrNull<ChapterData>(modelPosition)?.let {
                            requestChapterDataViewModel.authorChaptermovedown(novel.bid,it.cid,modelPosition)
                        }
                    }
                }
                //popupMenu(findView(R.id.imageViewGD),modelPosition)
            }

            //修改章节名
            R.id.imageView11.onClick{
                val data = getModel<ChapterData>()
                requireContext().changeDialog(getString(R.string.xiugaizhangdjiawesd),data.name){
                    requestChapterDataViewModel.authorEditchapterename(modelPosition,novel.bid,data.cid,it)
                }
            }

            //修改
            R.id.tvXiuGai.onClick {
                getModelOrNull<ChapterData>()?.let {
                    inChapterAddFragment(novel.bid,it)
                }
            }

            //回收
            R.id.tvShouHui.onClick {
                getModelOrNull<ChapterData>()?.let {
                    if(it.state==2){
                        it.state=0
                        requestChapterDataViewModel.authorChapterPublish(novel.bid,it.cid,modelPosition)
                    }else{
                        it.state=2
                        requestChapterDataViewModel.authorChapterPublish(novel.bid,it.cid,modelPosition)
                    }
                }
            }

            //删除
            R.id.tvSanChu.onClick {
                showMessage(getString(R.string.quedinhzhangjiesanshcusad), positiveButtonText = getString(
                    R.string.quedingawdsad), positiveAction = {
                    getModelOrNull<ChapterData>()?.let {
                        requestChapterDataViewModel.authorChapterDel(novel.bid,it.cid,modelPosition)
                    }
                }, negativeButtonText = getString(R.string.quxiaodawd))
            }
        }.models

        //状态页配置
        loadsir = loadServiceInit(mDatabind.includeList.includeRecyclerview.swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showLoading()
            questList()
        }

        //初始化 SwipeRefreshLayout
        mDatabind.includeList.includeRecyclerview.swipeRefresh.init {
            //触发刷新监听时请求数据
            questList()
        }

        mDatabind.includeList.includeRecyclerview.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                triggerSlide()
                changeBarPosition()
            }
        })

        mDatabind.scrollBar.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        cancelTask()
                        barTouchedLastY = event.rawY}
                    MotionEvent.ACTION_MOVE -> {
                        val dY: Float = event.rawY - barTouchedLastY
                        moveListView(dY)
                        // showToastView()
                        barTouchedLastY = event.rawY
                        //上下边界值
                        when {
                            mDatabind.scrollBar.y + dY + mDatabind.scrollBar.measuredHeight >= mDatabind.includeList.includeRecyclerview.recyclerView.height -> {
                                //到底了
                                mDatabind.scrollBar.y =
                                    mDatabind.includeList.includeRecyclerview.recyclerView.height.toFloat() - mDatabind.scrollBar.measuredHeight.toFloat()
                            }
                            mDatabind.scrollBar.y + dY <= 0 -> {
                                //到顶了
                                mDatabind.scrollBar.y = 0f
                            }
                            else -> {
                                mDatabind.scrollBar.y = mDatabind.scrollBar.y + dY
                            }
                        }
                    }
                    MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                        startTask()
                    }
                }
                return true
            }
        })
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        questList()
    }

    override fun createObserver() {
        requestChapterDataViewModel.authorChaptermoveupResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                    data->
                lazyLoadData()
            })
        }
        requestChapterDataViewModel.authorChaptermovedownResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                    data->
                lazyLoadData()
            })
        }

        requestChapterDataViewModel.authorChaptertimingResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                    data->
                mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.getModelOrNull<ChapterData>(data.position)?.let {
                    it.timing =stringToTimestamp(data.additional)
                    mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.notifyItemChanged(data.position)
                }
            }, {
                showMessage(it.errorMsg)
            })
        }

        requestChapterDataViewModel.authorChapterDelResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                    data->
                questList()
//                mDatabind.includeList.includeRecyclerview.recyclerView.mutable.removeAt(data.position)
//                mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.notifyItemRemoved(data.position)
            }, {
                showMessage(it.errorMsg)
            })
        }


        requestChapterDataViewModel.authorChapterPublishResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                    data->
                if(0==type){
                    mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.notifyItemChanged(data.position)
                }else{
                    mDatabind.includeList.includeRecyclerview.recyclerView.mutable.removeAt(data.position)
                    mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.notifyItemRemoved(data.position)
                    lazyLoadData()
                }
            })
        }

        requestChapterDataViewModel.authorEditchapterenameResult.observe(viewLifecycleOwner) {
            mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.getModel<ChapterData>(it.position).name = it.additional
            mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.notifyItemChanged(it.position)
        }

        requestChapterDataViewModel.authorChapterlistResult.observe(viewLifecycleOwner, Observer {resultState->
            parseState(resultState,mDatabind.includeList.includeRecyclerview.swipeRefresh,loadsir, {
                eventViewModel.allChapterNum.value = it.public+it.draft //发送通知
                eventViewModel.draftChapterNum.value = it.draft //发送通知

                val list = groupChapters(it.data)
                Dxresult.clear()
                Dxresult.addAll(list)
                listChapterData = list
                mDatabind.includeList.includeRecyclerview.recyclerView.models = setDataGro(list)
                if(it.data.size==0){
                    loadsir.showEmpty(SysConfig.NUll_CHAPTER_LIST)
                }
            },{},SysConfig.NUll_CHAPTER_LIST)
        })
    }

    inner class ProxyClick {

    }

    //请求列表
    private fun questList(){
        if(0==type){
            requestChapterDataViewModel.authorChapterlist(novel.bid)
        }else{
            requestChapterDataViewModel.authorChapterDraftlist(novel.bid)
        }
    }

    private fun groupChapters(result:ArrayList<ChapterData>):ArrayList<ChapterData>{
        result.sortBy { it.order }
        //-----------------给所有章节分组-----------------------
        val vList = result.filter { it.type==1 }
        val posList :ArrayList<Int> = ArrayList()
        if(vList.isNotEmpty()){//有卷
            vList.forEach { vData->
                val pos = result.indexOf(vData) //卷的位置
                posList.add(pos)
            }
        }

        for ((i, data) in posList.withIndex()) {
            if(i!=posList.size-1){
                for (pp in data..posList[i+1]){
                    result[pp].chapterData = result[data]
                }
            }else{
                for (pp in data until result.size){
                    result[pp].chapterData = result[data]
                }
            }
        }

        return result
        //-----------------给所有章节分组-----------------------
    }

    //数据分组
    private fun setDataGro(list:ArrayList<ChapterData>,isAn:Boolean=mViewModel.isReverseOrder.get()):ArrayList<ChapterAllData>{
        val result:ArrayList<ChapterData> = ArrayList()
        result.addAll(list)
        //顺序
        if(isAn){//升序
            result.sortBy { it.order }
        }else{
            result.sortByDescending { it.order }
        }

        //分割线
        result.forEach {
            it.lines = true
        }
        if(result.size>0){
            result[result.size-1].lines = false
        }

        val dataList :ArrayList<ChapterAllData> = ArrayList()
        val chapterDataList:ArrayList<ChapterData> = ArrayList()
        var chapterDataM :ChapterData?=null

        if(isAn){
            for ((i, chapterData) in result.withIndex()) {
            if(chapterData.type==1) {//是否为卷
                if(null!=chapterDataM){
                    val chapterDataX:ArrayList<ChapterData> = ArrayList()
                    chapterDataX.addAll(chapterDataList)

                    //分割线
                    if(chapterDataX.size>0){
                        chapterDataX[chapterDataX.size-1].lines = false
                    }
                    val chapterGroupModel = ChapterGroupModel()
                    chapterGroupModel.chapterData = chapterDataM
                    chapterGroupModel.jsonSublist = chapterDataX
                    dataList.add(chapterGroupModel)
                    chapterDataList.clear()
                }
                chapterDataM = chapterData
            }

            if(null!=chapterDataM){
                if(chapterData.type!=1){
                    chapterDataList.add(chapterData)
                }

                if(i==result.size-1){
                    val chapterDataX:ArrayList<ChapterData> = ArrayList()
                    chapterDataX.addAll(chapterDataList)

                    //分割线
                    if(chapterDataX.size>0){
                        chapterDataX[chapterDataX.size-1].lines = false
                    }
                    val chapterGroupModel = ChapterGroupModel()
                    chapterGroupModel.chapterData = chapterDataM
                    chapterGroupModel.jsonSublist = chapterDataX
                    dataList.add(chapterGroupModel)
                    chapterDataList.clear()
                }
                continue
            }

            dataList.add(chapterData)
        }
        }else{
            for ((i, chapterData) in result.withIndex()) {
                if(null!=chapterData.chapterData){//是否为卷中的章
                    if(chapterData.type==0){//且为章节，不是卷
                        chapterDataList.add(chapterData)
                    }else{//为卷
                        val chapterDataX:ArrayList<ChapterData> = ArrayList()
                        chapterDataX.addAll(chapterDataList)
                        val chapterGroupModel = ChapterGroupModel()
                        chapterGroupModel.chapterData = chapterData
                        chapterGroupModel.jsonSublist = chapterDataX
                        dataList.add(chapterGroupModel)
                        chapterDataList.clear()
                    }
                }else{
                    if(chapterData.type!=1) {//是否为卷
                        dataList.add(chapterData)
                    }
                }
            }
        }


        return dataList

//        val dataList :ArrayList<ChapterAllData> = ArrayList()
//        val chapterDataList:ArrayList<ChapterData> = ArrayList()
//        var chapterDataM :ChapterData?=null
//
//        for ((i, chapterData) in result.withIndex()) {
//            if(chapterData.type==1) {//是否为卷
//                if(null!=chapterDataM){
//                    val chapterDataX:ArrayList<ChapterData> = ArrayList()
//                    chapterDataX.addAll(chapterDataList)
//                    val chapterGroupModel = ChapterGroupModel()
//                    chapterGroupModel.chapterData = chapterDataM
//                    chapterGroupModel.jsonSublist = chapterDataX
//                    dataList.add(chapterGroupModel)
//                    chapterDataList.clear()
//                }
//                chapterDataM = chapterData
//            }
//
//            if(null!=chapterDataM){
//                if(chapterData.type!=1){
//                    chapterDataList.add(chapterData)
//                }
//
//                if(i==result.size-1){
//                    val chapterDataX:ArrayList<ChapterData> = ArrayList()
//                    chapterDataX.addAll(chapterDataList)
//                    val chapterGroupModel = ChapterGroupModel()
//                    chapterGroupModel.chapterData = chapterDataM
//                    chapterGroupModel.jsonSublist = chapterDataX
//                    dataList.add(chapterGroupModel)
//                    chapterDataList.clear()
//                }
//                continue
//            }
//
//            dataList.add(chapterData)
//        }

    }

    //弹窗
    fun popupMenu(view: View?, position:Int){
        if (view != null) {
            view.isActivated = true
        }
        val popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(R.menu.menu_item_push, popupMenu.menu)
        popupMenu.gravity = Gravity.END
        //弹出式菜单的菜单项点击事件
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {

                R.id.item_sy -> {
                    if(position!=0){
//                        //先记录要换位置的order
//                        val order1 =  mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.getModelOrNull<ChapterData>(position)?.order
//
//                        val order2 = mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.getModelOrNull<ChapterData>(position-1)?.order
//
//                        val data = mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.getModelOrNull<ChapterData>(position)
//
//                        mDatabind.includeList.includeRecyclerview.recyclerView.mutable.remove(data)
//
//                        order1?.let {
//                            mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.getModelOrNull<ChapterData>(position-1)?.order = order1
//                        }
//
//                        mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.notifyItemRemoved(position-1)
//
//                        mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.notifyItemRemoved(position)
//
//                        order2?.let {
//                            data?.order = order2
//                        }
//
//                        data?.let {
//                            val list :ArrayList<ChapterData> = ArrayList()
//                            list.add(data)
//                            mDatabind.includeList.includeRecyclerview.recyclerView.addModels(list,true,position-1)
//                            mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.notifyItemInserted(position-1)
//                        }

                        mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.getModelOrNull<ChapterData>(position)?.let {
                            requestChapterDataViewModel.authorChaptermoveup(novel.bid,it.cid,position)
                        }

                    }
                }
                R.id.item_xy -> {
                    mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.models?.let {
                        if(position!=it.size-1){
//                            //先记录要换位置的order
//                            val order1 = mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.getModelOrNull<ChapterData>(position)?.order
//                            val order2 =  mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.getModelOrNull<ChapterData>(position+1)?.order
//
//                            val data = mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.getModelOrNull<ChapterData>(position)
//
//                            mDatabind.includeList.includeRecyclerview.recyclerView.mutable.remove(data)
//
//                            order1?.let {
//                                mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.getModelOrNull<ChapterData>(position)?.order = order1
//                            }
//                            mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.notifyItemChanged(position+1)
//                            mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.notifyItemRemoved(position)
//
//
//                            if (order2 != null) {
//                                data?.order = order2
//                            }
//
//
//                            data?.let {
//                                val list :ArrayList<ChapterData> = ArrayList()
//                                list.add(data)
//                                mDatabind.includeList.includeRecyclerview.recyclerView.addModels(list,true,position+1)
//                                mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.notifyItemInserted(position+1)
//                            }

                            mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.getModelOrNull<ChapterData>(position)?.let {
                                requestChapterDataViewModel.authorChaptermovedown(novel.bid,it.cid,position)
                            }
                        }
                    }

                }
            }
            false
        }
        popupMenu.show()
    }

    /**
     * 时间选择
     * 默认是当前时间
     * */
    fun timeChoose(position:Int,time:Long){
        //开始时间
        val startDate = Calendar.getInstance()
        startDate[Calendar.MINUTE] = startDate[Calendar.MINUTE] + 1
        var defDate = startDate
        if(time!=0L){
            val date = Calendar.getInstance()
            date.time = timestampToTime(time)
            defDate=date
        }
        val endDate = Calendar.getInstance()
        endDate.time = startDate.time
        endDate[Calendar.YEAR] = endDate[Calendar.YEAR] + 2

        pvTime = TimePickerBuilder(activity, OnTimeSelectListener { date, v ->
            mDatabind.includeList.includeRecyclerview.recyclerView.bindingAdapter.getModelOrNull<ChapterData>(position)?.let {
                requestChapterDataViewModel.authorChaptertiming(position,novel.bid,it.cid,getDataToStringYMD(date))
            }
        })
            .setTimeSelectChangeListener { Log.i("pvTime", "onTimeSelectChanged") }
            .setType(booleanArrayOf(true, true, true, true, true, false))
            .isDialog(false) //默认设置false ，内部实现将DecorView 作为它的父控件。
            .addOnCancelClickListener { Log.i("pvTime", "onCancelClickListener") }
            .setItemVisibleCount(6) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
            .setLineSpacingMultiplier(3.0f)
            .isAlphaGradient(true)
            .setRangDate(startDate, endDate)
            .setDate(defDate)
            .setDecorView(activity?.window?.decorView?.findViewById(android.R.id.content))
            .build()

        val mDialog = pvTime?.dialog
        if (mDialog != null) {
            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.BOTTOM
            )
            params.leftMargin = 0
            params.rightMargin = 0
            pvTime?.dialogContainerLayout?.layoutParams = params
            val dialogWindow = mDialog.window
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim) //修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM) //改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f)
            }
        }
        pvTime?.getDialog()?.getWindow()?.getDecorView()?.setPadding(0, 0, 0, 0)
        pvTime?.show()
    }


    //滚动条
    private fun changeBarPosition() {
        //列表总长度
        val verticalScrollRange: Int =  mDatabind.includeList.includeRecyclerview.recyclerView.computeVerticalScrollRange()
        //可见区域长度
        val verticalScrollExtent: Int =  mDatabind.includeList.includeRecyclerview.recyclerView.computeVerticalScrollExtent()
        //向上滑动的大概距离
        val verticalScrollOffset: Int =  mDatabind.includeList.includeRecyclerview.recyclerView.computeVerticalScrollOffset()
        val listMovableHeight = verticalScrollRange - verticalScrollExtent
        val barMovableHeight: Int = verticalScrollExtent - mDatabind.scrollBar.height
        mDatabind.scrollBar.y = 1.0f * barMovableHeight / listMovableHeight * verticalScrollOffset
    }

    private fun moveListView(dY: Float) {
        //列表总长度
        val verticalScrollRange: Int = mDatabind.includeList.includeRecyclerview.recyclerView.computeVerticalScrollRange()
        //可见区域长度
        val verticalScrollExtent: Int = mDatabind.includeList.includeRecyclerview.recyclerView.computeVerticalScrollExtent()
        //向上滑动的大概距离
        val verticalScrollOffset: Int = mDatabind.includeList.includeRecyclerview.recyclerView.computeVerticalScrollOffset()
        val listMovableHeight = verticalScrollRange - verticalScrollExtent
        val barMovableHeight: Int = verticalScrollExtent - mDatabind.scrollBar.height
        mDatabind.includeList.includeRecyclerview.recyclerView.scrollBy(0, (1.0f * listMovableHeight / barMovableHeight * dY).toInt())
    }


    private fun triggerSlide(){
        cancelTask()
        startTask()
    }


    val handlerScrollBar = Handler()
    val runnable = Runnable {
        mDatabind.scrollBar.visibility = View.GONE //view是要隐藏的控件
    }
    private fun startTask(){
        mDatabind.scrollBar.visibility = View.VISIBLE
        handlerScrollBar.postDelayed(runnable, 1000) //3000毫秒后执行
    }

    private fun cancelTask(){
        handlerScrollBar.removeCallbacks(runnable);
        handlerScrollBar.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        handlerScrollBar.removeCallbacks(runnable);
        handlerScrollBar.removeCallbacksAndMessages(null)
    }
}