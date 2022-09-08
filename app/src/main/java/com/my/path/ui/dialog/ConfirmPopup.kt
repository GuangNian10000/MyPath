package com.my.path.ui.dialog

import android.content.Context
import android.view.View
import android.widget.TextView
import com.my.path.R
import com.my.path.app.ext.setVisibilityExt
import com.lxj.xpopup.core.CenterPopupView


/**
 * @author GuangNian
 * @description:
 * @date : 2021/5/28 2:53 下午
 */
class ConfirmPopup(context: Context,val title:String,val textDefault:String,val back: (Boolean) -> Unit = {}) : CenterPopupView(context) {

    override fun getImplLayoutId(): Int = R.layout.popup_window_confirm

    override fun onCreate() {
        super.onCreate()

        findViewById<TextView>(R.id.textView9).text = title
        findViewById<TextView>(R.id.editText).text = textDefault

        findViewById<TextView>(R.id.editText).setVisibilityExt(textDefault!="")

        findViewById<TextView>(R.id.textView32).setOnClickListener { v: View? ->
            back.invoke(true)
            dismiss() // 关闭弹窗
        }

        findViewById<TextView>(R.id.textView30).setOnClickListener { v: View? ->
            //back.invoke(false)
            dismiss() // 关闭弹窗
        }
    }
}