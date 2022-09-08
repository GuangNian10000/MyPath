package com.my.path.ui.fragment.setup

import android.os.Bundle
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.my.path.R
import com.my.path.app.appViewModel
import com.my.path.app.base.BaseFragment
import com.my.path.app.ext.hideSoftKeyboard
import com.my.path.app.ext.initTitle
import com.my.path.app.ext.showMessage
import com.my.path.app.util.*
import com.my.path.app.util.ViewTimingUtil.Companion.get
import com.my.path.databinding.FragmentEmailBindBinding
import com.my.path.viewmodel.request.RequestLoginRegisterViewModel
import com.my.path.viewmodel.state.BindEmailViewModel
import me.guangnian.mvvm.ext.nav
import me.guangnian.mvvm.ext.navigateAction
import me.guangnian.mvvm.ext.parseState


/**
 * @author　: GuangNian
 * @date　: 2019/12/24
 * @description　:
 */
class BindEmailFragment : BaseFragment<BindEmailViewModel, FragmentEmailBindBinding>() {

    private val requestLoginRegisterViewModel: RequestLoginRegisterViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel
        mDatabind.click = ProxyClick()

        mDatabind.includeToolbar.toolbar.initTitle(context?.getString(R.string.bangdingyouasdxd)) {
            nav().navigateUp()
        }
        appViewModel.run {
            userCountryCode.observeInFragment(this@BindEmailFragment, Observer {
                mViewModel.countryCode.set("+$it")
            })
            userCountryName.observeInFragment(this@BindEmailFragment, Observer {
                mViewModel.countryName.set(it)
            })
        }
    }

    override fun createObserver() {

        //发送验证码
        requestLoginRegisterViewModel.sendEmailCodeResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                get().getTimer(mDatabind.textView3).start()
            }, {
                showMessage(it.errorMsg)
            })
        }

        //注册
        requestLoginRegisterViewModel.emailCodeResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                nav().navigateUp()
            }, {
                showMessage(it.errorMsg)
            })
        }
    }


    inner class ProxyClick {
        fun goPhone(){
            hideSoftKeyboard(activity)
            requestLoginRegisterViewModel.registerBindemail(
                mViewModel.email.get(),
            mViewModel.emailCode.get())
            //nav().popBackStack()
          // nav().navigateAction(R.id.action_emailLoginFragment_to_registerFrgment)
        }

        fun shutDown(){
            nav().navigateUp()
        }

        /**
         * 发送验证码
         * */
        fun sendCode(){
            when {
                mViewModel.email.get().isEmpty() -> showMessage(getString(R.string.qinttiansahsouji))
                else -> requestLoginRegisterViewModel.registerSendemail(
                    mViewModel.email.get()
                )
            }
        }

        /**
         * 发送语音验证码
         * */
        fun registerSendvoice(){
            when {
                mViewModel.phoneReg.get().isEmpty() -> showMessage(getString(R.string.qinttiansahsouji))
                else -> requestLoginRegisterViewModel.registerSendvoice(
                    mViewModel.countryCode.get().replace("+",""),
                    mViewModel.phoneReg.get()
                )
            }
        }

        /**
         * 选则国别码
         * */
        fun countryCode() {
            hideSoftKeyboard(activity)
            nav().navigateAction(R.id.areaCodeFragment)
        }
        /**清空*/
        fun clear() {
            mViewModel.username.set("")
        }

        /**注册*/
        fun register() {
            when {
                mViewModel.phoneReg.get().isEmpty() -> showMessage(getString(R.string.qinttiansahsouji))
                mViewModel.passwordReg.get().isEmpty() -> showMessage(getString(R.string.qingtianxiemia))
                mViewModel.passwordReg.get().length < 6 -> showMessage(getString(R.string.mimazuishaoasd))
                mViewModel.countryCode.get().isEmpty()-> showMessage(getString(R.string.guobiemabuzhengq))
                else -> requestLoginRegisterViewModel.registerEmail(
                    mViewModel.email.get(),
                    mViewModel.emailCode.get(),
                    mViewModel.emailPassword.get(),
                    ""
                )
            }
        }

        var onCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                mViewModel.isShowPwd.set(isChecked)
            }


        var onCheckedChangeListener1 = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            mViewModel.isShowPwd.set(isChecked)
        }
        var onCheckedChangeListener2 = CompoundButton.OnCheckedChangeListener { _, isChecked ->
            mViewModel.isShowPwd2.set(isChecked)
        }
    }
}