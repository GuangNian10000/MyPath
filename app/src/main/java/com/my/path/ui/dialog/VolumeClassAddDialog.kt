package com.my.path.ui.dialog

import android.app.Activity
import android.widget.EditText
import android.widget.TextView
import com.my.path.R
import com.my.path.app.ext.clickWithDebounce
import com.my.path.app.ext.getBackgroundExt
import com.my.path.app.ext.getStringNoBlank
import com.my.path.app.ext.makeToast
import com.my.path.viewmodel.state.chapter.ChapterAddViewModel
import com.lxj.xpopup.core.BottomPopupView

class VolumeClassAddDialog(var mActivity: Activity, val back: (String)->Unit) : BottomPopupView(mActivity){

    override fun getImplLayoutId():Int = R.layout.popup_window_volume_class_add

    override fun onCreate() {
        super.onCreate()

        findViewById<EditText>(R.id.tvContent).addTextChangedListener(object : ChapterAddViewModel.SimpleTextWatcher(){
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                val length = getStringNoBlank(s.toString()).length
                if(length==0){
                    findViewById<TextView>(R.id.tvBaocu).background = getBackgroundExt(R.drawable.bg_book_shanchusdasde_off)
                }else{
                    findViewById<TextView>(R.id.tvBaocu).background = getBackgroundExt(R.drawable.bg_book_shanchusdasde)
                }
            }
        })

        findViewById<EditText>(R.id.imageView22).clickWithDebounce {
            dismiss()
        }

        findViewById<EditText>(R.id.tvBaocu).clickWithDebounce {
            val text = findViewById<EditText>(R.id.tvContent).text.toString()
            if(""!=text){
                back.invoke(text)
                dismiss()
            }else{
                makeToast("不能为空")
            }

        }
    }
}
