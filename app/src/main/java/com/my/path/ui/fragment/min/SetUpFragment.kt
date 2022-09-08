package com.my.path.ui.fragment.min

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.ext.initTitle
import com.my.path.app.ext.showMessage
import com.my.path.app.util.*

import com.my.path.databinding.FragmentSetUpBinding
import com.my.path.viewmodel.request.SetUpRegisterViewModel
import com.my.path.viewmodel.state.SetUpViewModel
import me.guangnian.mvvm.ext.nav
import me.guangnian.mvvm.ext.navigateAction
import me.guangnian.mvvm.ext.parseState

/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　: 设置
 */
class SetUpFragment : BaseFragment<SetUpViewModel, FragmentSetUpBinding>() {
    private val setUpRegisterViewModel: SetUpRegisterViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(setUpRegisterViewModel)
        mDatabind.viewmodel = mViewModel
        mDatabind.click = ProxyClick()



        mDatabind.include.toolbar.initTitle(context?.getString(R.string.shezhiasdx)) {
            nav().navigateUp()
        }
    }

    override fun createObserver() {
        setUpRegisterViewModel.logoutResult.observe(viewLifecycleOwner, Observer { resultState ->
            parseState(resultState, {
                CacheUtil.setLoginInfo(null)
                CacheUtil.setIsLogin(false)
                nav().navigateUp()
            },{
            })
        })
    }

    inner class ProxyClick {

        //关于我们
        fun goAboutUs(){
            nav().navigateAction(R.id.aboutFragment)
        }

        //账户与安全
        fun goAccount(){
            nav().navigateAction(R.id.accountSecurityFragment)
        }

        fun goLanguages(){
            nav().navigateAction(R.id.languageFragment)
        }

        fun goShielding(){
            nav().navigateAction(R.id.shieldingFragment)
        }

        //退出登录
        fun logOut(){
            showMessage(getString(R.string.quedingtuiachuasd), positiveButtonText = getString(
                R.string.quedingawdsad), positiveAction = {
                setUpRegisterViewModel.logout()
            }, negativeButtonText = getString(R.string.quxiaodawd))

        }
    }
}