package com.my.path.ui.dialog

import android.app.Activity
import com.my.path.R
import com.lxj.xpopup.core.BubbleAttachPopupView

class LoginAttachDialog(mActivity: Activity) : BubbleAttachPopupView(mActivity){

    override fun getImplLayoutId():Int = R.layout.popup_window_login_attach
    override fun onCreate() {
        super.onCreate()
        initData()
    }

    private fun initData(){

    }
}
