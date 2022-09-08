package com.my.path.ui.fragment.me

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.config.SysConfig
import com.my.path.app.ext.*
import com.my.path.data.model.bean.Book
import com.my.path.data.model.bean.MeUpdate
import com.my.path.databinding.FragmentCalendarBinding
import com.my.path.ui.adapter.home.CalendarCodeAdapter
import com.my.path.ui.dialog.SwitchMeBookDialog
import com.my.path.viewmodel.request.RequestCalendarViewModel
import com.my.path.viewmodel.state.CalendarViewModel
import com.kingja.loadsir.core.LoadService
import com.lxj.xpopup.XPopup
import me.guangnian.mvvm.ext.parseState
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　: h5页面
 */
class CalendarFragment : BaseFragment<CalendarViewModel, FragmentCalendarBinding>() {

    private lateinit var loadsir: LoadService<Any>

    private val requestCalendarViewModel: RequestCalendarViewModel by viewModels()

    private val calendarCodeAdapter: CalendarCodeAdapter by lazy { CalendarCodeAdapter(arrayListOf()) }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel

        mDatabind.viewTitleText.title.text = getString(R.string.mazirilidasease)

        mDatabind.click = ProxyClick()

        //初始化recyclerView
        mDatabind.recyclerView.init(GridLayoutManager(context,7), calendarCodeAdapter)

       // requestCalendarViewModel.meGetbook()

        mDatabind.viewIn3.setOnTouchListener(ProxyClick().longMonth)
        mDatabind.viewNextDe.setOnTouchListener(ProxyClick().longMonth)

        //状态页配置
        loadsir = loadServiceInit(mDatabind.llContent) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestCalendarViewModel.meGetbook()
        }

        mDatabind.constraintLayout7.clickWithDebounce {
            if(null!= mViewModel.meGetBook){
                XPopup.Builder(context)
                    .asCustom(SwitchMeBookDialog(requireActivity(),mViewModel.meGetBook!!.books){ book->
                        requestData(book)
                    }).show()
            }else{
                requestCalendarViewModel.meGetbook()
            }
        }

        mDatabind.llContent.visibility = View.GONE
    }


    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        requestCalendarViewModel.meGetbook()
    }

    inner class ProxyClick {
        //上一个月
        fun inMonth(){
            val mData = SimpleDateFormat("yyyy-MM").parse(mViewModel.theYear.toString()+"-"+mViewModel.theMonth, ParsePosition(0))
            val date = Calendar.getInstance()
            date.time = mData
            date[Calendar.MONTH] = date[Calendar.MONTH] - 1

            mViewModel.book.get()?.let {
                val bid = it._id
                requestCalendarViewModel.meUpdate(bid.toString(), date[Calendar.YEAR].toString(),(date[Calendar.MONTH]+1).toString())
            }
        }

        //下一个月
        fun nextMonth(){
            val mData = SimpleDateFormat("yyyy-MM").parse(mViewModel.theYear.toString()+"-"+mViewModel.theMonth, ParsePosition(0))
            val date = Calendar.getInstance()
            date.time = mData
            date[Calendar.MONTH] = date[Calendar.MONTH] + 1

            //不能超过当前月份
            if(date[Calendar.YEAR]>= getYear().toInt() &&(date[Calendar.MONTH]+1)>getMonth().toInt()){
                return
            }

            mViewModel.book.get()?.let {
                val bid = it._id
                requestCalendarViewModel.meUpdate(bid.toString(), date[Calendar.YEAR].toString(),(date[Calendar.MONTH]+1).toString())
            }
        }

        val longMonth =object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                when (p1?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        p0?.id?.let {
                            val msg = Message()
                            msg.what = it
                            handler.sendMessage(msg)
                         //   updateAddOrSubtract(it)
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        stopAddOrSubtract()
                    }
                    else -> {}
                }
                return true
            }
        }
    }

    override fun createObserver() {
        requestCalendarViewModel.meGetbookResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState,loadsir, {
                mViewModel.meGetBook = it
                if(it.books.size>0){//默认请求第一本
                    val data = it.books[0]
                    requestData(data)
                }else{
                    loadsir.showEmpty(SysConfig.NUll_RIli_LIST)
                }
            },{},SysConfig.NUll_RIli_LIST)
        }

        requestCalendarViewModel.meUpdateResult.observe(viewLifecycleOwner) {resultState ->
            parseState(resultState, {
                //更新UI
                mViewModel.yearMonthTitle.set(it.year.toString()+"年-"+it.month.toString()+"月")
                mViewModel.theMonth = it.month
                mViewModel.theYear = it.year
                val meUpdateList :MutableList<MeUpdate> = ArrayList()
                val w = 7
                //空档天数
                var empty =0
                //总字数
                var sizeNum =0
                //打卡天数
                var dakNum = 0


                //查询的月的一号是星期几
                val beginDate = SimpleDateFormat("yyyy-MM-dd").parse(it.year.toString()+"-"+it.month+"-01", ParsePosition(0))
                val date = Calendar.getInstance()
                date.time = beginDate
                val Week = date[Calendar.DAY_OF_WEEK]
                val week = Week
                empty = w-week
                //添加空档日期
                for (i in 1..empty){
                    meUpdateList.add(MeUpdate(0,0,true,0))
                }
                //获取当前月有多少天，然后添加数据
                val theMonthDay = getMonthLastDay(it.year,it.month)
                for(i in 1..theMonthDay){
                    val y = getYear()
                    val m =  getMonth()
                    val d = getDay()
                    val isDya = it.year.toString()== y&&it.month.toString()==m&&i.toString()== d

                    var meUpdate : MeUpdate?=null
                    it.data.forEach {
                            dataDTO->
                        //月日
                        val mData = SimpleDateFormat("yyyy-MM-dd").parse(dataDTO.date, ParsePosition(0))
                        val date = Calendar.getInstance()
                        date.time = mData
                        val year = date[Calendar.YEAR]
                        val month = date[Calendar.MONTH]
                        val day = date[Calendar.DATE]

                        //对应日期
                        if(day==i){
                            meUpdate = MeUpdate(day,dataDTO.words.toLong(),false,dataDTO.highlight,isDya)
                            sizeNum+=dataDTO.words
                            if(dataDTO.words>0){
                                dakNum++
                            }
                        }
                    }
                    if(null==meUpdate){
                        meUpdate =  MeUpdate(i,0,false,0,isDya)
                    }
                    meUpdateList.add(meUpdate!!)
                }

                //更新统计
                mViewModel.sizeNum.set(sizeNum.toString())
                mViewModel.dayNum.set(dakNum.toString())
                calendarCodeAdapter.setList(meUpdateList)

                //颜色变化
                if(date[Calendar.YEAR]>= getYear().toInt() &&it.month>=getMonth().toInt()){
                    mDatabind.viewNext.background = context?.getDrawable(R.drawable.ic_youjianthuisad)
                }else{
                    mDatabind.viewNext.background = context?.getDrawable(R.drawable.ic_youjiantoulkiian)
                }
            },{
            })
        }
    }

    private fun requestData(data:Book){
        mViewModel.book.set(data)
        requestCalendarViewModel.meUpdate(data._id.toString(), getYear(), getMonth())
    }

    private var scheduledExecutor: ScheduledExecutorService? = null

    private fun updateAddOrSubtract(viewId: Int) {
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor()
        scheduledExecutor?.scheduleWithFixedDelay(Runnable {
            val msg = Message()
            msg.what = viewId
            handler.sendMessage(msg)
        }, 0, 500, TimeUnit.MILLISECONDS) //每间隔100ms发送Message
    }

    private fun stopAddOrSubtract() {
        if (scheduledExecutor != null) {
            scheduledExecutor?.shutdownNow()
            scheduledExecutor = null
        }
    }

    private val handler= object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val viewId = msg.what
            when (viewId) {
                mDatabind.viewIn3.id-> ProxyClick().inMonth()
                mDatabind.viewNext.id-> ProxyClick().nextMonth()
                mDatabind.viewNextDe.id-> ProxyClick().nextMonth()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAddOrSubtract()
        handler.removeCallbacksAndMessages(null)
    }
}