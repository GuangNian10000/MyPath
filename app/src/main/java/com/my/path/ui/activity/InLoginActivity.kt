package com.my.path.ui.activity

import android.content.Intent

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.my.path.app.base.BaseActivity
import com.my.path.app.config.SysConfig.AUTH_SECRET
import com.my.path.app.eventViewModel
import com.my.path.data.interfaces.OnClickQuickLogin
import com.my.path.databinding.ActivityInLoginTheBinding
import com.my.path.viewmodel.request.RequestLoginViewModel
import com.my.path.viewmodel.state.MainViewModel
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper
import com.mobile.auth.gatewayauth.PreLoginResultListener
import com.mobile.auth.gatewayauth.ResultCode
import com.mobile.auth.gatewayauth.TokenResultListener
import com.mobile.auth.gatewayauth.model.TokenRet
import me.guangnian.mvvm.ext.parseState

class InLoginActivity : BaseActivity<MainViewModel, ActivityInLoginTheBinding>() {
    private val requestLoginViewModel : RequestLoginViewModel by viewModels()

    private var TAG="LoginActivity"

    //一键登录
    private var sdkAvailable: Boolean = true
    private var mCheckListener: TokenResultListener? = null
    private var mPhoneNumberAuthHelper: PhoneNumberAuthHelper? = null
    private var mUIConfig: BaseUIConfig? = null
    private val onClickLogin = object : OnClickQuickLogin {
        override fun onClickFacebook() {
            
        }

        override fun onClickQQ() {
            
        }

        override fun onClickWX() {
            
        }

    }

    override fun onResume() {
        super.onResume()
        mUIConfig?.onResume()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun initView(savedInstanceState: Bundle?) {
        eventViewModel.isLogin.observeInActivity(this) { isLogin ->
            //监听登录状态，如果已经登录则退出页面
            if(isLogin){
                mUIConfig?.mAuthHelper?.quitLoginPage()
               finish()
            }
        }
        //一键登录
        if(null ==mUIConfig){
            sdkInit(AUTH_SECRET)
            mUIConfig = BaseUIConfig.init( this, mPhoneNumberAuthHelper,onClickLogin)
            mPhoneNumberAuthHelper = PhoneNumberAuthHelper.getInstance(this, mCheckListener)
            mPhoneNumberAuthHelper?.checkEnvAvailable()
            mUIConfig?.configAuthPage()
            getLoginToken(0)
        }
    }

    override fun createObserver() {
        requestLoginViewModel.registerOnekeyResult.observe(this
        ) { resultState ->
            parseState(resultState, {
                mPhoneNumberAuthHelper?.quitLoginPage()
                finish()
            }, {})
        }
    }
    /**
     * 一键登录
     * */
    fun sdkInit(secretInfo: String?) {
        mCheckListener = object : TokenResultListener {
            override fun onTokenSuccess(s: String) {
                try {
                    val pTokenRet = TokenRet.fromJson(s)
                    if (ResultCode.CODE_ERROR_ENV_CHECK_SUCCESS == pTokenRet.code) {
                        accelerateLoginPage(5000)
                    }
                    if (ResultCode.CODE_START_AUTHPAGE_SUCCESS == pTokenRet.code) {
                        Log.i("TAG", "唤起授权页成功：$s")
                    }

                    if (ResultCode.CODE_SUCCESS == pTokenRet.code) {
                        Log.i("TAG", "获取token成功：$s")
                        // getResultWithToken(tokenRet.getToken())
                        requestLoginViewModel.registerOnekey(pTokenRet.token)
                        mPhoneNumberAuthHelper!!.setAuthListener(null)
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onTokenFailed(s: String) {
                sdkAvailable = false
              //  makeToast("")
                //终端环境检查失败之后 跳转到其他号码校验方式
                //如果环境检查失败 使用其他登录方式
                var tokenRet: TokenRet? = null
                finish()
                try {
                    tokenRet = TokenRet.fromJson(s)
                    tokenRet.msg?.let {
                    //    makeToast(getString(R.string.sahdijxcyidnegshibai))
                    }
                    if (ResultCode.CODE_ERROR_USER_CANCEL != tokenRet.code) {
                        eventViewModel.onInLogin.postValue(true)
                        //    inLoginFragment()
                        //  IntentUtil().IntentLogin(this@InLoginActivity)
                    }else {

                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
                mPhoneNumberAuthHelper?.quitLoginPage()
                mPhoneNumberAuthHelper?.setAuthListener(null)

            }
        }
        mPhoneNumberAuthHelper = PhoneNumberAuthHelper.getInstance(this, mCheckListener)
        mPhoneNumberAuthHelper?.reporter?.setLoggerEnable(true)
        mPhoneNumberAuthHelper?.setAuthSDKInfo(secretInfo)
        mPhoneNumberAuthHelper?.checkEnvAvailable(PhoneNumberAuthHelper.SERVICE_TYPE_LOGIN)
    }

    /**
     * 在不是一进app就需要登录的场景 建议调用此接口 加速拉起一键登录页面
     * 等到用户点击登录的时候 授权页可以秒拉
     * 预取号的成功与否不影响一键登录功能，所以不需要等待预取号的返回。
     * @param timeout
     */
    fun accelerateLoginPage(timeout: Int) {
        mPhoneNumberAuthHelper!!.accelerateLoginPage(timeout, object : PreLoginResultListener {
            override fun onTokenSuccess(s: String) {
                Log.e(TAG, "预取号成功: $s")
            }

            override fun onTokenFailed(s: String, s1: String) {
                Log.e(TAG, "预取号失败：, $s1")
            }
        })
    }

    /**
     * 拉起授权页
     * @param timeout 超时时间
     */
    fun getLoginToken(timeout: Int) {
        mPhoneNumberAuthHelper?.getLoginToken(this, timeout)
        // showLoadingDialog("正在唤起授权页")
    }

//    override fun initView(savedInstanceState: Bundle?) {
//        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                val nav = Navigation.findNavController(this@InLoginActivity, R.id.host_fragment)
//                if (nav.currentDestination != null && nav.currentDestination!!.id != R.id.inLoginFragment) {
//                    //如果当前界面不是主页，那么直接调用返回即可
//                    nav.navigateUp()
//                } else {
//                   finish()
//                }
//            }
//        })
//    }
}