package com.my.path.ui.dialog

import android.app.Activity
import androidx.databinding.DataBindingUtil
import com.my.path.R
import com.my.path.data.model.bean.Tag
import com.my.path.databinding.PopupWindowBookMoreBinding
import com.drake.brv.utils.*
import com.lxj.xpopup.core.BottomPopupView


class BookMoreDialog(mActivity: Activity,
    private val onCallback :((Int) -> Unit)) : BottomPopupView(mActivity){

    private var binding: PopupWindowBookMoreBinding?=null

    private val mListYcData:ArrayList<Tag> = ArrayList()

    override fun getImplLayoutId():Int = R.layout.popup_window_book_more

    override fun onCreate() {
        super.onCreate()

        binding = DataBindingUtil.bind(popupImplView)

        initOnClick()
    }



    private fun initOnClick(){
        binding?.apply {
            tvQux.setOnClickListener {
                dismiss()
            }
            textView21.setOnClickListener {
                onCallback.invoke(1)
                dismiss()
            }
            textView22.setOnClickListener {
                onCallback.invoke(2)
                dismiss()
            }
        }
    }

}
