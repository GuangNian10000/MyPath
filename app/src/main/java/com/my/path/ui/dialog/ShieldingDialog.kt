package com.my.path.ui.dialog

import android.annotation.SuppressLint
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.my.path.R
import com.my.path.app.ext.makeToast
import com.my.path.app.ext.showMessage

import com.my.path.data.interfaces.PopupShieldingCallback
import com.my.path.data.model.bean.BaseCode
import com.drake.net.Post
import com.drake.net.utils.scope
import com.lxj.xpopup.core.BottomPopupView


class ShieldingDialog(var mActivity: Fragment, val userId:Int, val pos:Int,val popupShieldingCallback: PopupShieldingCallback) : BottomPopupView(mActivity.requireContext()){

    override fun getImplLayoutId():Int = R.layout.popup_window_shielding

    override fun onCreate() {
        super.onCreate()
        initData()
        initOnClick()
    }

    @SuppressLint("CutPasteId", "NotifyDataSetChanged")
    private fun initData(){
        findViewById<TextView>(R.id.tvQuXiao).setOnClickListener {
            dismiss()
        }
        findViewById<TextView>(R.id.tvTouSu).setOnClickListener {
            dismiss()
            mActivity.showMessage(mActivity.getString(R.string.qubaochenggasdwae))
        }
        findViewById<TextView>(R.id.tvPingBi).setOnClickListener {
            dismiss()
            scope {
                val bean = Post<BaseCode>("/flow/addblacklist/"){
                    param("did", userId)
                }.await()
                if(200==bean.st){
                    makeToast(bean.result)
                    popupShieldingCallback.shielding(userId,pos)
                }else{
                    makeToast(bean.msg)
                }
            }
        }
    }

    private fun initOnClick(){
    }
    
}
