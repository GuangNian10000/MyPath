package com.my.path.ui.fragment.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.config.SysConfig.HELP_CENTER
import com.my.path.app.ext.inCalendarFragment
import com.my.path.app.ext.showMessage
import com.my.path.app.util.*
import com.my.path.databinding.FragmentMeBinding
import com.my.path.viewmodel.request.RequestMeViewModel
import com.my.path.viewmodel.state.*
import com.my.path.viewmodel.state.home.MeViewModel
import me.guangnian.mvvm.ext.nav
import me.guangnian.mvvm.ext.navigateAction
import me.guangnian.mvvm.ext.parseState
import me.jessyan.retrofiturlmanager.RetrofitUrlManager

/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　: h5页面
 */
class MeFragment : BaseFragment<MeViewModel, FragmentMeBinding>() {

    private val requestMeViewModel: RequestMeViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {

        addLoadingObserve(requestMeViewModel)

        mDatabind.viewmodel = mViewModel
        mDatabind.proxyClick = ProxyClick()

        requestMeViewModel.meGetInFo()
    }

    inner class ProxyClick {
        //码字日历
        fun goCalendar(){
            nav().navigateAction(R.id.calendarFragment)
        }
        //编辑资料
        fun goModifyUser(){
            nav().navigateAction(R.id.modifyUserFragment)
        }
        //清理缓存
        fun clearCache(){
            showMessage(
                getString(R.string.qinglihuancasd),
                positiveButtonText = getString(R.string.qingliasxzcsd),
                negativeButtonText = getString(R.string.quxioasdcesf),
                positiveAction = {
                    activity?.let { CacheDataManager.clearAllCache(it as? AppCompatActivity) }
                })
        }
        fun inCalendar(){
            inCalendarFragment()
        }
        //设置
        fun goSetUp(){
            nav().navigateAction(R.id.setUpFragment)
        }

        //帮助中心
        fun goHelpCenter(){
            nav().navigateAction(R.id.webFragment, Bundle().apply {
                putString("title",getString(R.string.bangzhuzhongxinasd))
                putString("url",RetrofitUrlManager.getInstance().globalDomain.toString()+HELP_CENTER)
            })
        }

        //意见反馈
        fun goFeedBack(){
            nav().navigateAction(R.id.feedBackFragment)
        }
    }

    override fun lazyLoadData() {

    }

    override fun createObserver() {
        //用户信息
        requestMeViewModel.meGetInFoResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                mViewModel.userData.set(it)
                //设置笔名
                if(it.author=="0"){
//                    requireActivity().changePenNameDialog{
//                        requestMeViewModel.meGetInFo()
//                    }
                }
            })
        }
    }


    override fun onResume() {
        super.onResume()
        requestMeViewModel.meGetInFo()
    }
}