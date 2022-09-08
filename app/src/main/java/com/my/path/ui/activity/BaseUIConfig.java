package com.my.path.ui.activity;


import static com.lxj.xpopup.util.XPopupUtils.dp2px;

import android.app.Activity;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.my.path.R;
import com.my.path.data.interfaces.CustomXmlConfig;
import com.my.path.data.interfaces.OnClickQuickLogin;
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper;


public abstract class BaseUIConfig {
    public Activity mActivity;
    public PhoneNumberAuthHelper mAuthHelper;
    public int mScreenWidthDp;
    public int mScreenHeightDp;
    public OnClickQuickLogin onClickQuickLogin;
    public static BaseUIConfig init(Activity activity, PhoneNumberAuthHelper authHelper, OnClickQuickLogin onClickQuickLogin) {
        return new CustomXmlConfig(activity, authHelper,onClickQuickLogin);
    }

    public BaseUIConfig(Activity activity, PhoneNumberAuthHelper authHelper, OnClickQuickLogin onClickQuickLogin) {
        mActivity = activity;
        mAuthHelper = authHelper;
        this.onClickQuickLogin = onClickQuickLogin;
    }

    protected View initSwitchView(int marginTop) {
        TextView switchTV = new TextView(mActivity);
        RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, dp2px(mActivity, 50));
        //一键登录按钮默认marginTop 270dp
        mLayoutParams.setMargins(0, dp2px(mActivity, marginTop), 0, 0);
        mLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        switchTV.setText( Html.fromHtml(mActivity.getString(R.string.yiyouzhangshd)+"<font color=\"#FF9D69\">"+mActivity.getString(R.string.denglusadxsa)+ "<font/>"));
        switchTV.setTextColor(mActivity.getColor(R.color.shape_reading_set_bg_22));
        switchTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        switchTV.setLayoutParams(mLayoutParams);
        return switchTV;
    }


    public abstract void configAuthPage();

    /**
     *  在横屏APP弹竖屏一键登录页面或者竖屏APP弹横屏授权页时处理特殊逻辑
     *  Android8.0只能启动SCREEN_ORIENTATION_BEHIND模式的Activity
     */
    public void onResume() {

    }
}
