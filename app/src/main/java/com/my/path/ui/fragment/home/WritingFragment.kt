package com.my.path.ui.fragment.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.config.SysConfig
import com.my.path.app.eventViewModel
import com.my.path.app.ext.*
import com.my.path.databinding.FragmentWritingBinding
import com.my.path.ui.adapter.home.WritingAdapter
import com.my.path.viewmodel.request.RequestMeViewModel
import com.my.path.viewmodel.request.home.RequestWritingViewModel
import com.my.path.viewmodel.state.home.WritingViewModel
import com.kingja.loadsir.core.LoadService
import com.zhpan.bannerview.constants.PageStyle
import com.zhpan.indicator.enums.IndicatorSlideMode
import me.guangnian.mvvm.ext.parseState
import me.guangnian.mvvm.ext.util.dp2px

/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　: h5页面
 */
class WritingFragment : BaseFragment<WritingViewModel, FragmentWritingBinding>() {

    private val requestWritingViewModel: RequestWritingViewModel by viewModels()

    private val requestMeViewModel: RequestMeViewModel by viewModels()

    private var writingAdapter: WritingAdapter?=null

    private lateinit var loadsir: LoadService<Any>

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.viewmodel = mViewModel

        mDatabind.proxyClick = ProxyClick()

        addLoadingObserve(requestWritingViewModel)
        addLoadingObserve(requestMeViewModel)

        requestMeViewModel.meGetInFo() //请求用户信息

        //监听添加写作
        eventViewModel.inPenName.observeInFragment(this){
            requestMeViewModel.meGetInFo() //请求用户信息
        }

        eventViewModel.inAddWriting.observeInFragment(this){
            inWritingAddFragment()
        }

        eventViewModel.writingEvent.observeInFragment(this){
            lazyLoadData()
        }

        //状态页配置
        loadsir = loadServiceInit(mDatabind.constraint) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestWritingViewModel.authorIndex()
        }

        initBannerViewPage()
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        requestWritingViewModel.authorIndex()
    }

    inner class ProxyClick {
        fun inWritingAdd(){
            inWritingAddFragment()
        }

        fun inCalendar(){
            inCalendarFragment()
        }
    }

    override fun createObserver() {
        requestWritingViewModel.authorBookdelResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, {
                requestWritingViewModel.authorIndex()
            },{})
        }

        requestMeViewModel.meGetInFoResult.observe(viewLifecycleOwner){ resultState ->
            parseState(resultState,loadsir, {
                if(""==it.author||"0"==it.author){
                    loadsir.showPenName(getString(R.string.qingshezhibimsae))
                    requireActivity().changePenNameDialog{
                        requestWritingViewModel.authorIndex()
                    }
                }else{
                    requestWritingViewModel.authorIndex()
                }
            },{})
        }

        requestWritingViewModel.authorIndexResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState,loadsir, {
                if(it.isNotEmpty()){
                    mViewModel.mBookList = it
                    mDatabind.bannerView.data.clear()
                    mDatabind.bannerView.addData(it)
                }else{
                    loadsir.showEmpty(SysConfig.NUll_WRItING_LIST)
                }
            },{},SysConfig.NUll_WRItING_LIST)
        }
    }

    /**
     * 初始化
     * */
    private fun initBannerViewPage(){
        mDatabind.bannerView.apply {
            writingAdapter = WritingAdapter(this@WritingFragment){type,novel->
                when(type){
                    1->{
                        inWritingChangesFragment(novel)
                    }
                    2->{
                        requestWritingViewModel.authorBookdel(novel.bid)
                    }
                }
            }
            setAdapter(writingAdapter)
            setLifecycleRegistry(lifecycle)
            setCanLoop(false)
            setAutoPlay(false)
            //指示器是否可见
            setIndicatorVisibility(View.GONE)
            //滑动模式
            setIndicatorSlideMode(IndicatorSlideMode.NORMAL)
                //样式
                .setPageStyle(PageStyle.NORMAL)
                //页面改变监听
                . registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    @SuppressLint("SetTextI18n")
                    override fun onPageSelected(position: Int) {
//                        if(position>0){
//                            setRevealWidth(dp2px(50),dp2px(50))
//                        }else{
//                            setRevealWidth(0,dp2px(50))
//                        }
                    }})
            .setRevealWidth(dp2px(40),dp2px(25))
            .create()
        }
    }


    override fun onResume() {
        super.onResume()
        lazyLoadData()
    }
}