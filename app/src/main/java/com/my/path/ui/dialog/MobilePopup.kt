package com.my.path.ui.dialog

import android.content.Context
import android.view.View
import com.my.path.R
import com.my.path.app.ext.clickWithDebounce
import com.lxj.xpopup.core.AttachPopupView

/**
 * @author GuangNian
 * @description:
 * @date : 2022/8/25 2:14 下午
 */
class MobilePopup(context: Context,val back: (Boolean) -> Unit = {}) : AttachPopupView(context) {
    override fun getImplLayoutId(): Int = R.layout.popup_window__book_mobile

    override fun onCreate() {
        super.onCreate()
        findViewById<View>(R.id.view7).clickWithDebounce {
            back.invoke(false)
            dismiss()
        }

        findViewById<View>(R.id.view30).clickWithDebounce {
            back.invoke(true)
            dismiss()
        }
    }
}