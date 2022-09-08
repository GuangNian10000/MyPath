package com.my.path.ui.fragment.login

import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.widget.ScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.my.path.R
import com.my.path.app.appViewModel
import com.my.path.app.base.BaseFragment
import com.my.path.app.eventViewModel
import com.my.path.app.ext.*
import com.my.path.app.util.*
import com.my.path.app.util.ViewTimingUtil.Companion.get
import com.my.path.databinding.FragmentRegisteredBinding
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
class RegisteredFragment : BaseFragment<RegisteredViewModel, FragmentRegisteredBinding>() {

    private val phoneViewModel:PhoneViewModel by viewModels()

    private val codeViewModel:CodeViewModel  by viewModels()

    private val userServiceAgreementViewModel: UserServiceAgreementViewModel by viewModels()

    private val requestLoginViewModel: RequestLoginViewModel by viewModels()


    val fullScroll = Runnable {
            mDatabind.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }

    val handler =Handler()

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.viewmodel = mViewModel

        mDatabind.phoneViewModel = phoneViewModel

        mDatabind.codeViewModel = codeViewModel

        mDatabind.userServiceViewModel = userServiceAgreementViewModel

        mDatabind.proxyClick = ProxyClick()

        addLoadingObserve(requestLoginViewModel)

        eventViewModel.isLogin.observeInFragment(this) { isLogin ->
            //监听登录状态，如果已经登录则退出页面
            nav().navigateUp()
        }

        appViewModel.run {
            userCountryCode.observeInFragment(this@RegisteredFragment, Observer {
                phoneViewModel.ipCode.set("+$it")
            })
            userCountryName.observeInFragment(this@RegisteredFragment, Observer {
                phoneViewModel.region.set(it)
            })
        }

        handler.post(fullScroll);

        mDatabind.viewTextInputRegistered.constraintRegion.clickWithDebounce {
            ProxyClick().countryCode()
        }

        mDatabind.viewTextInputPwd.tvSend.clickWithDebounce {
            ProxyClick().sendCode()
        }

        mDatabind.viewTextInputPwd.textView29.clickWithDebounce {
            ProxyClick().registerSendvoice()
        }

        mDatabind.viewTextInputPwd.textView31.clickWithDebounce {
            inRegisteredEmailFragment()
        }

        requestLoginViewModel.ip2country()

        mDatabind.viewTextInputPwd.textView29.text = Html.fromHtml(getString(R.string.shishisyanzm)+"<font color=\"#FF6464\">"+getString(R.string.yuyingyanzmasd)+ "<font/>")

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
                get().getTimer2(mDatabind.viewTextInputPwd.tvSend,mDatabind.viewTextInputPwd.textView29).start()
            }, {
                showMessage(it.errorMsg)
            })
        }

        //发送语音验证码
        requestLoginViewModel.sendVoiceCode.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                get().getTimerVoice(mDatabind.viewTextInputPwd.textView29,mDatabind.viewTextInputPwd.tvSend).start()
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
                else -> requestLoginViewModel.registerSendcode(
                    phoneViewModel.ipCode.get().replace("+",""),
                    phoneViewModel.userName.get()
                )
            }
        }

        //发送语音验证码
        fun registerSendvoice(){
            when {
                phoneViewModel.userName.get().isEmpty() -> showMessage(getString(R.string.tianxieshoujiasdawe))
                else -> requestLoginViewModel.registerSendvoice(
                    phoneViewModel.ipCode.get().replace("+",""),
                    phoneViewModel.userName.get()
                )
            }
        }

    }

    private fun check(){
        if(""!=phoneViewModel.region.get()&&
            ""!=phoneViewModel.ipCode.get()&&
            ""!=phoneViewModel.userName.get()&&
            ""!=codeViewModel.verificationCode.get()&&
            ""!=codeViewModel.password.get()){
            requestLoginViewModel.register(
                phoneViewModel.ipCode.get().replace("+",""),
                phoneViewModel.userName.get(),
                codeViewModel.verificationCode.get(),
                codeViewModel.password.get())
        }else{
            makeToast(getString(R.string.qingianxiwancsa))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(fullScroll);
        handler.removeCallbacksAndMessages(null)
    }
}