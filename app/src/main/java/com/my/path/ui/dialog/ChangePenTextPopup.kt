package com.my.path.ui.dialog

import android.app.Activity
import androidx.databinding.DataBindingUtil
import com.my.path.R
import com.my.path.app.ext.hideSoftKeyboard
import com.my.path.app.ext.makeToast
import com.my.path.app.ext.showInputMethod
import com.my.path.data.model.bean.BaseCode
import com.my.path.databinding.PopupWindowChangeTextBinding
import com.drake.net.Post
import com.drake.net.utils.scope
import com.lxj.xpopup.core.CenterPopupView

/**
 * @author GuangNian
 * @description:
 * @date : 2021/5/28 2:53 下午
 */
class ChangePenTextPopup(
    private val mContext: Activity,
    private val onCallback :((String) -> Unit)) : CenterPopupView(mContext) {

    override fun getImplLayoutId(): Int = R.layout.popup_window_change_text

    private var binding: PopupWindowChangeTextBinding?=null


    override fun onCreate() {
        super.onCreate()
        binding = DataBindingUtil.bind(popupImplView)
        binding?.apply {
            tvTitle.text = context.getString(R.string.shezhibindjhasjeasd)
            tvContent.setSelection(tvContent.text.length)//光标始终保持在输入文本末尾
            tvContent.hint = context.getString(R.string.bimingjzhjkdashe)
            tvQd.setOnClickListener {
                if(tvContent.text.toString()==""){
                    makeToast(context.getString(R.string.neijkdaksde))
                    return@setOnClickListener
                }
                setPenName(tvContent.text.toString())
            }
            context.showInputMethod(tvContent)
        }
    }

    private fun setPenName(name:String){
        scope {
            val bean = Post<BaseCode>("/me/saveauthor/"){
                param("author", name)
            }.await()
            if(200==bean.st){
                onCallback.invoke(name)
                dismiss()
            }else{
                makeToast(bean.msg)
            }
        }
    }

    override fun onDismiss() {
        binding?.apply {
            context.hideSoftKeyboard(tvContent)
        }
        super.onDismiss()
    }
}