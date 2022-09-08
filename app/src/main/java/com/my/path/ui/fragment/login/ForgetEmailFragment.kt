package com.my.path.ui.fragment.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.eventViewModel
import com.my.path.app.ext.*
import com.my.path.app.util.*
import com.my.path.app.util.ViewTimingUtil.Companion.get
import com.my.path.databinding.FragmentForgetPwdEmailBinding
import com.my.path.viewmodel.request.RequestLoginViewModel
import com.my.path.viewmodel.state.*
import com.my.path.viewmodel.state.login.UserServiceAgreementViewModel
import me.guangnian.mvvm.ext.nav
import me.guangnian.mvvm.ext.navigateAction
import me.guangnian.mvvm.ext.parseState


/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　: 登录
 */
class ForgetEmailFragment : BaseFragment<RegisteredViewModel, FragmentForgetPwdEmailBinding>() {

    private val phoneViewModel:PhoneViewModel by viewModels()

    private val codeViewModel:CodeViewModel  by viewModels()

    private val userServiceAgreementViewModel: UserServiceAgreementViewModel by viewModels()

    private val requestLoginViewModel: RequestLoginViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {

        eventViewModel.isLogin.observeInFragment(this) { isLogin ->
            //监听登录状态，如果已经登录则退出页面
            nav().navigateUp()
        }

        mDatabind.include7.tvTitle.text =getString(R.string.wangjimiamsdase)

        mDatabind.viewmodel = mViewModel

        mDatabind.phoneViewModel = phoneViewModel

        mDatabind.codeViewModel = codeViewModel

        mDatabind.userServiceViewModel = userServiceAgreementViewModel

        mDatabind.proxyClick = ProxyClick()

        addLoadingObserve(requestLoginViewModel)

        mDatabind.viewTextInputPwd.tvSend.clickWithDebounce {
            ProxyClick().sendCode()
        }
        mDatabind.viewTextInputPwd.textView31.clickWithDebounce {
            nav().navigateUp()
        }

        mDatabind.viewTextInputPwd.textView29.visibility = View.GONE
        mDatabind.viewTextInputPwd.textView31.text = getString(R.string.shojihaozhaodjawe)
      //  mDatabind.viewTextInputPwd.textView29.text = Html.fromHtml(getString(R.string.shishisyanzm)+"<font color=\"#FF6464\">"+getString(R.string.yuyingyanzmasd)+ "<font/>")

    }

    override fun lazyLoadData() {

    }

    override fun createObserver() {

        //国别码
        requestLoginViewModel.codeResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, {
                phoneViewModel.ipCode.set("+"+it.country)
                phoneViewModel.region.set(AreaCodeUtil(activity).getAreaCodeCity(it.country))
            })
        }

        //选择地区
//        requestLoginViewModel.loginResult.observe(viewLifecycleOwner) { resultState ->
//            nav().navigateUp()
//        }

        //发送验证码
        requestLoginViewModel.sendCode.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                get().getTimer(mDatabind.viewTextInputPwd.tvSend).start()
            }, {
                showMessage(it.errorMsg)
            })
        }
    }

    inner class ProxyClick {

        //注册
        fun registered(){
            if(!mDatabind.userServiceViewModel!!.isChecked.get()){
                requireActivity().showService(mDatabind.viewBottomAgreement.llAgreement,mDatabind.viewBottomAgreement.checkBox)
            }else{
                check()
            }
        }

        //选择地区
        fun countryCode() {
            hideSoftKeyboard(activity)
            nav().navigateAction(R.id.areaCodeFragment)
        }

        //发送验证码
        fun sendCode(){
            when {
                phoneViewModel.userName.get().isEmpty() -> showMessage(getString(R.string.tianxieshoujiasdawe))
                else -> requestLoginViewModel.sendbindemail(phoneViewModel.userName.get())
            }
        }
    }

    private fun check(){
        if(""!=phoneViewModel.userName.get()&&
            ""!=codeViewModel.verificationCode.get()&&
            ""!=codeViewModel.password.get()){
            requestLoginViewModel.editpassEmail(
                phoneViewModel.userName.get(),
                codeViewModel.verificationCode.get(),
                codeViewModel.password.get())
        }else{
            makeToast(getString(R.string.qingianxiwancsa))
        }
    }
}