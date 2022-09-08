package com.my.path.ui.fragment.chapter

import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.eventViewModel
import com.my.path.app.ext.*
import com.my.path.app.util.IndentTextWatcher
import com.my.path.app.util.SettingUtil.getWriteTemporaryCache
import com.my.path.app.util.SettingUtil.setWriteTemporaryNUll
import com.my.path.app.util.ait.AitEditText
import com.my.path.app.util.ait.AitpeopleUtil
import com.my.path.app.util.ait.RangBean
import com.my.path.data.model.bean.*
import com.my.path.databinding.FragmentChapterAddBinding
import com.my.path.ui.dialog.VolumeClassDialog
import com.my.path.viewmodel.request.chapter.RequestChapterDataViewModel
import com.my.path.viewmodel.state.chapter.ChapterAddViewModel
import com.lxj.xpopup.XPopup
import me.guangnian.mvvm.ext.nav
import me.guangnian.mvvm.ext.parseState

/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　: h5页面
 */
class ChapterAddFragment(): BaseFragment<ChapterAddViewModel, FragmentChapterAddBinding>() {

    private val requestChapterDataViewModel: RequestChapterDataViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.viewmodel = mViewModel

        addLoadingObserve(requestChapterDataViewModel)

        eventViewModel.onSaveChapter.observeInFragment(this){
            ProxyClick().back()
        }

        mDatabind.include4.imageView4.clickWithDebounce {
            ProxyClick().back()
        }

        arguments?.run {
            mViewModel.bid = getInt("bid",0)//书id
            mViewModel.chapterContent.set(ChapterContent())//默认值

            /**
             * 创建章节才会读取缓存
             * */
            if(mViewModel.chapterData.cid==0){
                val temporary = getWriteTemporaryCache(requireContext())
                temporary?.let {
                    //未上传到草稿到数据
                    if(""==it.cid){
                        //读本地
                        mViewModel.chapterContent.set(it)
                    }
                }
            }

            getParcelable<ChapterData>("chapterData")?.let {//章节信息
                mViewModel.chapterData = it
                if(mViewModel.chapterData.cid!=0){
                    mViewModel.title.set("修改章节")
                    requestChapterDataViewModel.getArticleContent(mViewModel.bid,mViewModel.chapterData.cid)
                }
            }

            getParcelableArrayList<Volume>("mVolumeList")?.let {list->//章节信息
                mViewModel.chapterContent.get()?.let {
                    it.volume = list
                }
            }
        }

        mDatabind.textView7.clickWithDebounce {
            ProxyClick().release()
        }

        mDatabind.textView6.clickWithDebounce {
            ProxyClick().draft()
        }

        mDatabind.tvVolume.clickWithDebounce {
            ProxyClick().showVolume()
        }

        mDatabind.editText.addTextChangedListener(IndentTextWatcher())
    }

    //校验
    private fun check():Boolean{
        mViewModel.chapterContent.get()?.apply {
            return when {
                ""==obName -> {
                    showMessage(getString(R.string.biaotibunengweikongasd))
                    false
                }
                ""==obContent -> {
                    showMessage(getString(R.string.neirnongabunegasdawe))
                    false
                }
                else -> true
            }
        }
       return true
    }

    inner class ProxyClick {
        fun back(){
            //返回
            showMessage(getString(R.string.shifoubaofunasdhnierasd), positiveButtonText = getString(
                R.string.baocundawaddawed), positiveAction = {
                if(mViewModel.chapterContent.get()!!.cid!=""){
                    release()
                }else{
                    draft()
                }
            }, negativeButtonText = getString(R.string.bubaocunasdaweaw),negativeAction = {
                setWriteTemporaryNUll(requireContext())
                nav().navigateUp()
            })
        }

        fun showVolume(){

            mViewModel.chapterContent.get()?.let {chapterCon->
                chapterCon.volume.forEach {
                    it.select = false
                }
                chapterCon.volume.forEach {
                    if(it.cid.toString()==chapterCon.obVcid){
                        it.select = true
                    }
                }
                XPopup
                    .Builder(requireContext())
                    .asCustom(VolumeClassDialog(this@ChapterAddFragment,mViewModel.bid, chapterCon.volume){
                        chapterCon.obVcName = it.name //更改卷名，和卷id
                        chapterCon.obVcid = it.cid.toString()
                    }).show()
            }
        }

        fun release(){
            val mCid = if(0!=mViewModel.chapterData.cid){
                mViewModel.chapterData.cid.toString()
            }else{
               ""
            }
            mViewModel.chapterContent.get()?.apply {
                showMessage(getString(R.string.fabudaweasd), positiveButtonText = getString(
                    R.string.quedingawdsad), positiveAction = {
                    if(check()){
                        requestChapterDataViewModel.authorSavechapter(mViewModel.bid,
                            obName,
                            obContent,
                            obSay,1, obVcid,mCid
                        )
                    }
                }, negativeButtonText = getString(R.string.quxiaodawd))
            }
        }

        //草稿 1为公开，0位草稿
        fun draft(){
            val mCid = if(0!=mViewModel.chapterData.cid){
                mViewModel.chapterData.cid.toString()
            }else{
                ""
            }
            mViewModel.chapterContent.get()?.apply {
                showMessage(getString(R.string.cuncaogdawe), positiveButtonText = getString(
                    R.string.quedingawdsad), positiveAction = {
                    if(check()){
                        requestChapterDataViewModel.authorSavechapter(mViewModel.bid,
                            obName,
                            obContent,
                            obSay,0,obVcid, mCid
                        )
                    }
                }, negativeButtonText = getString(R.string.quxiaodawd))
            }
        }
    }

    override fun createObserver() {
        requestChapterDataViewModel.authorSavechapterResult.observe(
            this
        ) { resultState ->
            parseState(resultState, {
                    data->
                if(data.st==200){
                    //有可能是草稿也可能是发布章节
                    eventViewModel.draftBoxEvent.value = true
                    nav().navigateUp()
                }else if(data.st==500){
                    makeToast(data.msg)
                    getStringUt(data.content,mDatabind.editText)
                    mDatabind.editText.setSelection(0)
                }else {
                    makeToast(data.msg)
                }
            })
        }

        requestChapterDataViewModel.getArticleContentResult.observe(viewLifecycleOwner, Observer {resultState->
            parseState(resultState, {
                mViewModel.chapterContent.set(it)
            },{
                showMessage("获取章节内容失败请重试", positiveButtonText = getString(
                R.string.quedingawdsad), positiveAction = {
                    requestChapterDataViewModel.getArticleContent(mViewModel.bid,mViewModel.chapterData.cid)
            }, negativeButtonText = getString(R.string.quxiaodawd))})
        })
    }


    fun getStringUt(text: String, aitView: AitEditText) {
        // 激活点击事件
        aitView.movementMethod = LinkMovementMethod.getInstance()
        val spannableStr: SpannableString = AitpeopleUtil.getSpStr(this,text,aitView){

        }
        aitView.setText(spannableStr)
        // 去掉文字块点击出现背景色
      //  aitView.highlightColor =getColor(android.R.color.transparent)
    }

    /**
     * 获取上传给服务端的格式化数据
     * @return String
     */
    private fun getUploadFormatText(): String {
        val text: String = mDatabind.editText.text.toString()
        mDatabind.editText.mRangeManager.sort()
        var lastRangeTo = 0
        val builder = StringBuilder("")
        var newChar: String
        for (range in mDatabind.editText.mRangeManager) {
            if(range.from<0){
                range.from=0
            }
            if(lastRangeTo>range.from){
                lastRangeTo = range.from
            }
            if(range.from>text.length){
                range.from = text.length
            }
            builder.append(text.substring(lastRangeTo, range.from))
            // 获取需要上传给后端的数据格式
            newChar = range.uploadFormatText
            builder.append(newChar)
            lastRangeTo = range.to
            if(lastRangeTo>text.length){
                lastRangeTo = text.length
            }
        }
        builder.append(text.substring(lastRangeTo))
        return builder.toString()
    }

    /**
     * 每删除一个字符，都要遍历缓存队列，判断是否是删除了队列中的数据
     * 如果删除是文字块前面的文字，对于后面的文字块要往前移位
     * 如果是删除文字块，则把文字块在缓存列表删除后，对于后面的文字块要往前移位
     * @param start
     * @param end
     * @param offset
     */
    private fun whenDelText(start: Int, end: Int, offset: Int) {
        val iterator: MutableIterator<*> = mDatabind.editText.mRangeManager.iterator()
        while (iterator.hasNext()) {
            val rangBean: RangBean = iterator.next() as RangBean
            // 判断起始位置是否包裹了文字块，如果包裹了，则把文字块相关信息在内存列表删除
            if (rangBean.isWrapped(start, end)) {
                iterator.remove()
                continue
            }
            // 将end之后的span，挪动offset个位置
            if (rangBean.getFrom() >= end) {
                rangBean.setOffset(offset)
            }
        }
    }
}