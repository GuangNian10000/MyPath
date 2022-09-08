package com.my.path.ui.dialog

import android.widget.EditText
import androidx.fragment.app.Fragment
import com.my.path.R
import com.my.path.app.ext.clickWithDebounce
import com.my.path.app.ext.makeToast
import com.my.path.app.ext.showConfirmPopup
import com.lxj.xpopup.core.BottomPopupView

class VolumeClassChangeDialog(var mActivity: Fragment, val content : String, val back: (String, Boolean)->Unit) : BottomPopupView(mActivity.requireContext()){

    override fun getImplLayoutId():Int = R.layout.popup_window_volume_class_change

    override fun onCreate() {
        super.onCreate()

        findViewById<EditText>(R.id.tvContent).setText(content)
        findViewById<EditText>(R.id.tvContent).setSelection(findViewById<EditText>(R.id.tvContent).text.length)//光标始终保持在输入文本末尾

        findViewById<EditText>(R.id.imageView22).clickWithDebounce {
            dismiss()
        }


        findViewById<EditText>(R.id.tvShanchu).clickWithDebounce {
            mActivity.showConfirmPopup("确定要删除\n"+content+"吗?","") {
                back.invoke("",true)
                dismiss()
            }
//            showMessage("确定要删除\n"+content+"吗？", positiveButtonText = mActivity.getString(
//                R.string.quedingawdsad), positiveAction = {
//                back.invoke("",true)
//                dismiss()
//            }, negativeButtonText =  mActivity.getString(R.string.quxiaodawd))
        }

        findViewById<EditText>(R.id.tvBaocu).clickWithDebounce {
            val text = findViewById<EditText>(R.id.tvContent).text.toString()
            if(""!=text){
                back.invoke(text,false)
                dismiss()
            }else{
                makeToast("不能为空")
            }

        }
    }
}
