package com.my.path.ui.fragment.setup

import android.os.Bundle
import android.text.Html
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
import com.my.path.databinding.FragmentPhoneBindBinding
import com.my.path.viewmodel.request.RequestLoginRegisterViewModel
import com.my.path.viewmodel.state.BindPhoneViewModel
import me.guangnian.mvvm.ext.nav
import me.guangnian.mvvm.ext.navigateAction
import me.guangnian.mvvm.ext.parseState


/**
 * @author　: GuangNian
 * @date　: 2019/12/24
 * @description　:
 */
class BindPhoneFragment : BaseFragment<BindPhoneViewModel, FragmentPhoneBindBinding>() {

    private val requestLoginRegisterViewModel: RequestLoginRegisterViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel
        mDatabind.click = ProxyClick()
        mDatabind.includeToolbar.toolbar.initTitle(context?.getString(R.string.bangdingsahoudas)) {
            nav().navigateUp()
        }
        appViewModel.run {
            userCountryCode.observeInFragment(this@BindPhoneFragment, Observer {
                mViewModel.countryCode.set("+$it")
            })
            userCountryName.observeInFragment(this@BindPhoneFragment, Observer {
                mViewModel.countryName.set(it)
            })
        }

        //获取货国别码
        requestLoginRegisterViewModel.ip2country()
        mDatabind.tvYyCode.text = Html.fromHtml(getString(R.string.shishisyanzm)+"<font color=\"#FF6464\">"+getString(R.string.yuyingyanzmasd)+ "<font/>")
        mDatabind.tvIntentLogin.text = Html.fromHtml(
            getString(R.string.yiyouzhangshd) + "<font color=\"#FF9D69\">" + getString(R.string.denglusadxsa) + "<font/>"
        )
    }

    override fun createObserver() {
        //国别码
        requestLoginRegisterViewModel.codeResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                mViewModel.countryCode.set("+"+it.country)
                mViewModel.countryName.set(AreaCodeUtil(activity).getAreaCodeCity(it.country))
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


        requestLoginRegisterViewModel.sendEmailCodeResult.observe(
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

        fun goLogin(){
            hideSoftKeyboard(activity)
            val phone: String = mViewModel.phoneReg.get()
            if (phone == "") {
                showMessage(getString(R.string.shoujihobudnegasd))
                return
            }
            val code = mViewModel.phoneCode.get()
            if(""==code){
                showMessage(getString(R.string.yanzhengamasd))
                return
            }
            requestLoginRegisterViewModel.registerBind(mViewModel.countryCode.get().replace("+",""),phone,code)
        }

        fun shutDown(){
            hideSoftKeyboard(activity)
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

        /**登录*/
        fun login() {
            when {
                mViewModel.phoneReg.get().isEmpty() -> showMessage(getString(R.string.qinttiansahsouji))
                mViewModel.countryCode.get().isEmpty()-> showMessage(getString(R.string.guobiemabuzhengq))
                else -> requestLoginRegisterViewModel.loginCode(
                    mViewModel.phoneReg.get(),
                    mViewModel.username.get(),
                    mViewModel.countryCode.get().replace("+","")
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