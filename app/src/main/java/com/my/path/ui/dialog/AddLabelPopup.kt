package com.my.path.ui.dialog

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.my.path.R
import com.my.path.app.ext.hideSoftKeyboard
import com.my.path.data.model.bean.PopupCallback
import com.lxj.xpopup.core.CenterPopupView

/**
 * @author GuangNian
 * @description:
 * @date : 2021/5/28 2:53 下午
 */
class AddLabelPopup(context: Context) : CenterPopupView(context) {

    private var maxNum = 5

    private var title =""

    private var textDefault=""

    private var popupCallback: PopupCallback?=null
    constructor(context: Activity,title:String, textDefault:String,maxNum: Int,popupCallback: PopupCallback) : this(context) {
        this.maxNum = maxNum
        this.title = title
        this.textDefault = textDefault
        this.popupCallback = popupCallback
    }

    override fun getImplLayoutId(): Int = R.layout.popup_window_add_label

    override fun onCreate() {
        super.onCreate()

        val et  = findViewById<EditText>(R.id.editText)

        et.setText(textDefault)
        findViewById<EditText>(R.id.editText).setSelection(findViewById<EditText>(R.id.editText).text.length);

        findViewById<TextView>(R.id.textView9).text = title

        findViewById<Button>(R.id.butIn).setOnClickListener { v: View? ->
            if(""!=et.text.toString()){
                popupCallback?.callback(et.text.toString())
            }
            dismiss() // 关闭弹窗
        }
    }

    override fun onDismiss() {
        findViewById<EditText>(R.id.editText)?.apply {
            context.hideSoftKeyboard(this)
        }
        super.onDismiss()
    }
}