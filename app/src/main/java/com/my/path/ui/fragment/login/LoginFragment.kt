package com.my.path.ui.fragment.login

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.eventViewModel
import com.my.path.app.ext.*
import com.my.path.app.util.*
import com.my.path.databinding.FragmentLoginBinding
import com.my.path.viewmodel.request.RequestLoginViewModel
import com.my.path.viewmodel.state.*
import com.my.path.viewmodel.state.login.UserServiceAgreementViewModel
import me.guangnian.mvvm.ext.nav


/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　: 登录
 */
class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    private val userServiceAgreementViewModel: UserServiceAgreementViewModel by viewModels()

    private val requestLoginViewModel: RequestLoginViewModel by viewModels()


    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel
        mDatabind.userServiceViewModel = userServiceAgreementViewModel

        mDatabind.proxyClick = ProxyClick()


        addLoadingObserve(requestLoginViewModel)
        eventViewModel.isLogin.observeInFragment(this) { isLogin ->
            //监听登录状态，如果已经登录则退出页面
            nav().navigateUp()
            onDestroy()
        }
    }

    override fun lazyLoadData() {

    }

    override fun createObserver() {
//        requestLoginViewModel.loginResult.observe(viewLifecycleOwner) { resultState ->
//            nav().navigateUp()
//        }
    }

    inner class ProxyClick {
        //登录
        fun login(){
            if(!mDatabind.userServiceViewModel!!.isChecked.get()){
                requireActivity().showService(mDatabind.viewBottomAgreement.llAgreement,mDatabind.viewBottomAgreement.checkBox)
            }else if(!mViewModel.bgLoginBut.get()){
                makeToast(getString(R.string.qingianxiwancsa))
            }else{
                requestLoginViewModel.login(mViewModel.userName.get(),mViewModel.password.get())
            }
        }

        fun inRegistered(){
            inRegisteredFragment()
        }

        fun inForgetPwd(){
            inForgetPwdFragment()
        }

        fun inLoginCode(){
            inLoginCodeFragment()
        }

    }
}