package com.my.path.ui.dialog

import android.app.Activity
import android.widget.TextView
import com.my.path.R
import com.my.path.app.ext.clickWithDebounce

import com.lxj.xpopup.core.BubbleAttachPopupView



class CommentSortingDialog(mActivity: Activity, var isOrderType:String, private val onCallback :((Int) -> Unit)) : BubbleAttachPopupView(mActivity){
    override fun getImplLayoutId():Int = R.layout.popup_window_comment_sorting
    override fun onCreate() {
        super.onCreate()
        initData()
        initOnClick()

    }

    private fun initData(){
        when(isOrderType){
            "default"->{
                findViewById<TextView>(R.id.tv_angengx).isSelected=false
                findViewById<TextView>(R.id.tv_anyuedupaixu).isSelected=false
                findViewById<TextView>(R.id.tv_zuixinshouc).isSelected=true
            }
            "new"->{
                findViewById<TextView>(R.id.tv_angengx).isSelected=true
                findViewById<TextView>(R.id.tv_anyuedupaixu).isSelected=false
                findViewById<TextView>(R.id.tv_zuixinshouc).isSelected=false
            }
            "hot"->{
                findViewById<TextView>(R.id.tv_zuixinshouc).isSelected=false
                findViewById<TextView>(R.id.tv_anyuedupaixu).isSelected=true
                findViewById<TextView>(R.id.tv_zuixinshouc).isSelected=false
            }
        }
    }

    private fun initOnClick(){
        findViewById<TextView>(R.id.tv_angengx).clickWithDebounce {
            onCallback.invoke(1)
            dismiss()
        }
        findViewById<TextView>(R.id.tv_anyuedupaixu).clickWithDebounce {
            onCallback.invoke(2)
            dismiss()
        }
        findViewById<TextView>(R.id.tv_zuixinshouc).clickWithDebounce {
            onCallback.invoke(0)
            dismiss()
        }
    }
}
