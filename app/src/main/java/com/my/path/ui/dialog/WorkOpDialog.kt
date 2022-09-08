package com.my.path.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.*
import com.my.path.R
import com.my.path.app.ext.hideSoftKeyboard
import com.lxj.xpopup.core.BottomPopupView

class WorkOpDialog(var mActivity: Activity,val content:String, val back: (String)->Unit) : BottomPopupView(mActivity){

    override fun getImplLayoutId():Int = R.layout.popup_window_work_op

    override fun onCreate() {
        super.onCreate()
        initData()
    }

    @SuppressLint("CutPasteId", "NotifyDataSetChanged")
    private fun initData(){
        val etContent = findViewById<EditText>(R.id.et_content)

        etContent.setText(content)

        etContent.hint = context.getString(R.string.shurutuijansdase)
        
        findViewById<EditText>(R.id.et_content).setSelection(findViewById<EditText>(R.id.et_content).text.length);

        findViewById<TextView>(R.id.tv_quxiao).setOnClickListener {
            dismiss()
        }

        findViewById<TextView>(R.id.tv_tijiao).setOnClickListener {
            val str = findViewById<TextView>(R.id.et_content).text
            back.invoke(str.toString())
            dismiss()
        }
    }
    override fun onDismiss() {
        findViewById<EditText>(R.id.et_content)?.apply {
            context.hideSoftKeyboard(this)
        }
        super.onDismiss()
    }
}
