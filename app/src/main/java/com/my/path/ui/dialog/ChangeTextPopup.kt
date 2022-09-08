package com.my.path.ui.dialog

import android.content.Context
import androidx.databinding.DataBindingUtil
import com.my.path.R
import com.my.path.app.ext.hideSoftKeyboard
import com.my.path.app.ext.makeToast
import com.my.path.app.ext.showInputMethod
import com.my.path.databinding.PopupWindowChangeTextBinding
import com.lxj.xpopup.core.CenterPopupView

/**
 * @author GuangNian
 * @description:
 * @date : 2021/5/28 2:53 下午
 */
class ChangeTextPopup(context: Context,
                      val title:String,
                      private val default:String="",
                      private val onCallback :((String) -> Unit)) : CenterPopupView(context) {

    override fun getImplLayoutId(): Int = R.layout.popup_window_change_text

    private var binding: PopupWindowChangeTextBinding?=null


    override fun onCreate() {
        super.onCreate()
        binding = DataBindingUtil.bind(popupImplView)
        binding?.apply {

            tvTitle.text = title
            tvContent.setText(default)
            tvContent.setSelection(tvContent.text.length)//光标始终保持在输入文本末尾

            tvQd.setOnClickListener {
                if(tvContent.text.toString()==""){
                    makeToast(context.getString(R.string.neijkdaksde))
                    return@setOnClickListener
                }
                onCallback.invoke(tvContent.text.toString())
                dismiss()
            }
            context.showInputMethod(tvContent)
        }
    }

    override fun onDismiss() {
        binding?.apply {
            context.hideSoftKeyboard(tvContent)
        }
        super.onDismiss()
    }
}