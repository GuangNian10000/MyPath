package com.my.path.ui.dialog

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import com.my.path.R
import com.my.path.data.model.bean.*
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.lxj.xpopup.core.BottomPopupView

class SwitchMeBookDialog(var mActivity: Activity, val mList : ArrayList<Book>, val back: (Book)->Unit) : BottomPopupView(mActivity){

    override fun getImplLayoutId():Int = R.layout.popup_window_book

    override fun onCreate() {
        super.onCreate()

        findViewById<RecyclerView>(R.id.recyclerView).linear().setup {

            addType<Book>(R.layout.item_writing_class_me_book)

            R.id.constraint.onClick {
                getModelOrNull<Book>()?.let {
                    back.invoke(it)
                }
                dismiss()
            }
        }.models=mList

    }
}
