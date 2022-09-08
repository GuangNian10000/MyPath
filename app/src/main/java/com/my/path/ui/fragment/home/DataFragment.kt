package com.my.path.ui.fragment.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.my.path.app.base.BaseFragment
import com.my.path.app.config.SysConfig
import com.my.path.app.ext.*
import com.my.path.app.util.*
import com.my.path.databinding.FragmentDataBinding
import com.my.path.ui.adapter.home.DataAdapter
import com.my.path.viewmodel.request.data.RequestDataViewModel
import com.my.path.viewmodel.state.*
import com.my.path.viewmodel.state.home.DataViewModel
import com.kingja.loadsir.core.LoadService
import com.zhpan.bannerview.constants.PageStyle
import com.zhpan.indicator.enums.IndicatorSlideMode
import me.guangnian.mvvm.ext.util.dp2px

/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　: h5页面
 */
class DataFragment : BaseFragment<DataViewModel, FragmentDataBinding>() {

    private lateinit var loadsir: LoadService<Any>

    private var dataAdapter: DataAdapter?=null

    private val requestDataViewModel:RequestDataViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {

        addLoadingObserve(requestDataViewModel)

        mDatabind.viewmodel = mViewModel

        //状态页配置
        loadsir = loadServiceInit(mDatabind.constraint) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestDataViewModel.dataBase()
        }

        initBannerViewPage()
    }

//    override fun lazyLoadData() {
//        loadsir.showLoading()
//        requestDataViewModel.dataBase()
//    }

    override fun onResume() {
        super.onResume()
        loadsir.showLoading()
        requestDataViewModel.dataBase()
    }

    override fun createObserver() {
        requestDataViewModel.dataBaseResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState,loadsir, {
                mDatabind.bannerView.data.clear()
                mDatabind.bannerView.addData(it.data)
                if(it.data.size==0){
                    loadsir.showEmpty(SysConfig.NUll_HOME_DATA)
                }
            },{},SysConfig.NUll_HOME_DATA)
        }
    }

    /**
     * 初始化
     * */
    private fun initBannerViewPage(){
        mDatabind.bannerView.apply {
            dataAdapter = DataAdapter(this@DataFragment)
            setAdapter(dataAdapter)
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

                    }})
                .setRevealWidth(dp2px(40),dp2px(30))
                .create()
        }
    }
}