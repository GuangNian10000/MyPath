package com.my.path.data.interfaces

import android.app.Activity
import android.content.Context


import org.json.JSONObject
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import android.util.Log
import com.mobile.auth.gatewayauth.ui.AbstractPnsViewDelegate
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.lxj.xpopup.util.XPopupUtils
import android.widget.Toast
import android.widget.TextView
import android.view.Gravity
import android.view.View
import com.my.path.R
import com.my.path.app.config.SysConfig
import com.my.path.app.config.SysConfig.yinSiUrl
import com.my.path.app.eventViewModel
import com.my.path.ui.activity.BaseUIConfig
import com.mobile.auth.gatewayauth.*
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import org.json.JSONException

/**
 * xml文件方便预览
 * 可以通过addAuthRegisterXmlConfig一次性统一添加授权页的所有自定义view
 */
class CustomXmlConfig(
    activity: Activity?,
    authHelper: PhoneNumberAuthHelper?,
    onClickQuickLogin: OnClickQuickLogin?
) : BaseUIConfig(activity, authHelper, onClickQuickLogin) {
    var TAG = "CustomXmlConfig"
    var isCheck = false
    override fun configAuthPage() {
        mAuthHelper.setUIClickListener { code, context, jsonString ->
            var jsonObj: JSONObject? = null
            try {
                if (!TextUtils.isEmpty(jsonString)) {
                    jsonObj = JSONObject(jsonString)
                }
            } catch (e: JSONException) {
                jsonObj = JSONObject()
            }
            when (code) {
                ResultCode.CODE_ERROR_USER_CANCEL -> {
                    Log.e(TAG, "点击了授权页默认返回按钮")
                    mAuthHelper.quitLoginPage()
                    mActivity.finish()
                }
                ResultCode.CODE_ERROR_USER_SWITCH -> Log.e(TAG, "点击了授权页默认切换其他登录方式")
                ResultCode.CODE_ERROR_USER_LOGIN_BTN -> if (!jsonObj!!.optBoolean("isChecked")) {
                    //   Toast.makeText(mContext, R.string.custom_toast, Toast.LENGTH_SHORT).show();
                }
                ResultCode.CODE_ERROR_USER_CHECKBOX -> {
                    isCheck = jsonObj!!.optBoolean("isChecked")
                    Log.e(TAG, "checkbox状态变为" + jsonObj.optBoolean("isChecked"))
                }
                ResultCode.CODE_ERROR_USER_PROTOCOL_CONTROL -> Log.e(
                    TAG,
                    "点击协议，" + "name: " + jsonObj!!.optString("name") + ", url: " + jsonObj.optString(
                        "url"
                    )
                )
                else -> {}
            }
        }
        mAuthHelper.removeAuthRegisterXmlConfig()
        mAuthHelper.removeAuthRegisterViewConfig()
        var authPageOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        if (Build.VERSION.SDK_INT == 26) {
            authPageOrientation = ActivityInfo.SCREEN_ORIENTATION_BEHIND
        }
        mAuthHelper.addAuthRegistViewConfig("number_logo", AuthRegisterViewConfig.Builder()
            .setView(initNumberView())
            .setRootViewId(AuthRegisterViewConfig.RootViewId.ROOT_VIEW_ID_NUMBER)
            .setCustomInterface { context: Context? -> }.build()
        )
        mAuthHelper.addAuthRegisterXmlConfig(
            AuthRegisterXmlConfig.Builder()
                .setLayout(R.layout.activity_login_quick, object : AbstractPnsViewDelegate() {
                    override fun onViewCreated(view: View) {
                        //                        findViewById(R.id.tv_switch).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toast.makeText(mContext, "切换到短信登录方式", Toast.LENGTH_SHORT).show();
//                                Intent pIntent = new Intent(mActivity, MessageActivity.class);
//                                mActivity.startActivityForResult(pIntent, 1002);
//                                mAuthHelper.quitLoginPage();
//                            }
//                        });
                        findViewById(R.id.image).setOnClickListener { v: View? ->
                            mActivity.finish()
                            mAuthHelper.quitLoginPage()
                        }
                    }
                })
                .build()
        )
        mAuthHelper.addAuthRegistViewConfig("switch_msg", AuthRegisterViewConfig.Builder()
            .setView(initSwitchView(330))
            .setRootViewId(AuthRegisterViewConfig.RootViewId.ROOT_VIEW_ID_BODY)
            .setCustomInterface {
                mActivity.finish()
                eventViewModel.onInLogin.value = true
                //IntentUtil().IntentLogin(mActivity)
                mAuthHelper.quitLoginPage()
            }
            .build())
        mAuthHelper.setAuthUIConfig(
            AuthUIConfig.Builder()
                .setAppPrivacyOne(
                    mActivity.getString(R.string.yonghuxieyizdas),
                    RetrofitUrlManager.getInstance().globalDomain.toString() + SysConfig.serviceAgreementUrl
                )
                .setAppPrivacyTwo(
                    mActivity.getString(R.string.yingsizhengscsad),
                    RetrofitUrlManager.getInstance().globalDomain.toString() + yinSiUrl
                )
                .setAppPrivacyColor(Color.GRAY, Color.parseColor("#FF6464"))
                .setNavHidden(true)
                .setLogoHidden(false)
                .setSloganHidden(true)
                .setSwitchAccHidden(false)
                .setPrivacyState(false)
                .setCheckboxHidden(false)
                .setLightColor(true) //状态栏
                // .setStatusBarUIFlag(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
                .setWebViewStatusBarColor(Color.TRANSPARENT)
                .setWebNavTextColor(Color.WHITE)
                .setWebNavTextSize(mActivity.getColor(R.color.public_title_text))
                .setStatusBarColor(Color.TRANSPARENT)
                .setStatusBarUIFlag(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
                .setWebNavTextSizeDp(20)
                .setNumberSizeDp(20)
                .setNumberColor(Color.BLACK)
                .setAuthPageActIn("in_activity", "out_activity")
                .setAuthPageActOut("in_activity", "out_activity")
                .setVendorPrivacyPrefix("《")
                .setVendorPrivacySuffix("》")
                .setLogoImgPath("ic_login_new") //logo
                .setLogoOffsetY(60) //num
                .setNumFieldOffsetY(154) //登录按钮
                .setLogBtnBackgroundPath("bg_button")
                .setLogBtnTextSizeDp(16)
                .setLogBtnMarginLeftAndRight(24)
                .setLogBtnHeight(48)
                .setLogBtnOffsetY(267) //其它登录方式
                .setSwitchAccText("已有账号？去登录")
                .setSwitchAccTextColor(mActivity.getColor(R.color.shape_reading_set_bg_22))
                .setSwitchAccTextSizeDp(14)
                .setSwitchOffsetY(330)
                .setSwitchAccHidden(true) //选中框
                .setCheckedImgPath("ic_check_book_on")
                .setUncheckedImgPath("ic_check_book_off")
                .setScreenOrientation(authPageOrientation)
                .create()
        )
    }

    private fun initNumberView(): View {
//        TextView pImageView = new TextView(mContext);
//        pImageView.setText("换号");
//        pImageView.setTextSize(14);
//        pImageView.setTextColor(mContext.getColor(R.color.login_sad));
//        RelativeLayout.LayoutParams pParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT);
//        pParams.addRule(RelativeLayout.CENTER_VERTICAL);
//        pParams.addRule(RelativeLayout.ALIGN_RIGHT);
//
//       // pParams.setMargins(dp2px(mContext, 30), 0, 0, 0);
//        pImageView.setLayoutParams(pParams);
       // val screenWidthDp: Int = AppUtils.px2dp(mContext, AppUtils.getPhoneWidthPixels(mContext))
        val v: View = LayoutInflater.from(mActivity)
            .inflate(R.layout.custom_login_config, RelativeLayout(mActivity), false)
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            XPopupUtils.dp2px(mActivity, 80f)
        )
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
        v.layoutParams = layoutParams
        v.findViewById<View>(R.id.tvHuan).setOnClickListener {
            mActivity.finish()
            eventViewModel.onInLogin.value  = false
          //  mActivity.inLoginPhoneFragment()
            //IntentUtil().IntentPhoneCodeLoginActivity(mActivity)
            mAuthHelper.quitLoginPage()
        }
        return v
    }

    fun makeToast(str: String?) {
        shoToast(str, Toast.LENGTH_SHORT, false)
    }

    fun shoToast(message: String?, duration: Int, failure: Boolean?) {
        val inflater = mActivity.layoutInflater
        val view = inflater.inflate(R.layout.view_custom_toast, null)
        val tv_sign_name = view.findViewById<TextView>(R.id.tv_name)
        tv_sign_name.text = message
        if (null != mActivity) {
            val toast = Toast(mActivity)
            toast.duration = duration
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.setView(view)
            toast.show()
        }
    }
}