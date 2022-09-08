package com.my.path.ui.fragment.setup

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.viewModels
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.ext.hideSoftKeyboard
import com.my.path.app.ext.showMessage
import com.my.path.app.util.*
import com.my.path.app.util.ViewTimingUtil.Companion.get
import com.my.path.databinding.FragmentForgetPasswordSetBinding
import com.my.path.viewmodel.request.RequestLoginRegisterViewModel
import com.my.path.viewmodel.state.ForgetPwdViewModel
import me.guangnian.mvvm.ext.nav
import me.guangnian.mvvm.ext.navigateAction
import me.guangnian.mvvm.ext.parseState

/**
 * @author　: GuangNian
 * @date　: 2019/12/24
 * @description　:
 */
class SetRetrievePasswordFragment : BaseFragment<ForgetPwdViewModel, FragmentForgetPasswordSetBinding>() {

    private val requestLoginRegisterViewModel: RequestLoginRegisterViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel
        mDatabind.click = ProxyClick()

        //获取货国别码
        requestLoginRegisterViewModel.ip2country()
        mDatabind.tvYyCode.text = Html.fromHtml(getString(R.string.shishisyanzm)+"<font color=\"#FF6464\">"+getString(R.string.yuyingyanzmasd)+ "<font/>")
    }

    override fun createObserver() {
        //国别码
        requestLoginRegisterViewModel.codeResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                mViewModel.countryCode.set("+"+it.country)
            }, {
                showMessage(it.errorMsg)
            })
        }

        //发送验证码
        requestLoginRegisterViewModel.sendCode.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                get().getTimer2(mDatabind.textView3,mDatabind.tvYyCode).start()
            }, {
                showMessage(it.errorMsg)
            })
        }

        //发送语音验证码
        requestLoginRegisterViewModel.sendCode.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                get().getTimerVoice(mDatabind.tvYyCode,mDatabind.textView3).start()
            }, {
                showMessage(it.errorMsg)
            })
        }

        //找回密码并登录
        requestLoginRegisterViewModel.loginResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
//                CacheUtil.setIsLogin(true)
//                CacheUtil.setUser(it)
//                appViewModel.userInfo.value = it
//                nav().navigateUp()
            }, {
                showMessage(it.errorMsg)
            })
        }
    }


    inner class ProxyClick {

        fun shutDown(){
            nav().navigateUp()
        }

        /**
         * 发送验证码
         * */
        fun sendCode(){
            when {
                mViewModel.phoneReg.get().isEmpty() -> showMessage(getString(R.string.qinttiansahsouji))
                else -> requestLoginRegisterViewModel.registerSendcode(
                    mViewModel.countryCode.get().replace("+",""),
                    mViewModel.phoneReg.get()
                )
            }
        }

        /**
         * 发送语音验证码
         * */
        fun registerSendvoice(){
            when {
                mViewModel.phoneReg.get().isEmpty() -> showMessage(getString(R.string.qinttiansahsouji))
                else -> requestLoginRegisterViewModel.registerSendcode(
                    mViewModel.countryCode.get().replace("+",""),
                    mViewModel.phoneReg.get()
                )
            }
        }

        /**
         * 选则国别码
         * */
        fun countryCode(){
            hideSoftKeyboard(activity)
            nav().navigateAction(R.id.areaCodeFragment)
        }

        /**找回密码并登录*/
        fun retrievePassword() {
            when {
                mViewModel.phoneReg.get().isEmpty() -> showMessage(getString(R.string.qinttiansahsouji))
                mViewModel.passwordReg.get().isEmpty() -> showMessage(getString(R.string.qingtianxiemia))
                mViewModel.passwordReg.get().length < 6 -> showMessage(getString(R.string.mimazuishaoasd))
                mViewModel.countryCode.get().isEmpty()-> showMessage(getString(R.string.guobiemabuzhengq))
                else -> requestLoginRegisterViewModel.editpass(
                    mViewModel.countryCode.get().replace("+",""),
                    mViewModel.phoneReg.get(),
                    mViewModel.phoneCode.get(),
                    mViewModel.passwordReg.get(),
                    mViewModel.passwordReg.get()
                )
            }
        }
    }
}