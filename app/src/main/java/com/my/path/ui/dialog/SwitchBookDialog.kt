package com.my.path.ui.dialog

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import com.my.path.R
import com.my.path.data.model.bean.BaseData
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.lxj.xpopup.core.BottomPopupView

class SwitchBookDialog(var mActivity: Activity, val mList : ArrayList<BaseData>, val back: (BaseData)->Unit) : BottomPopupView(mActivity){

    override fun getImplLayoutId():Int = R.layout.popup_window_book

    override fun onCreate() {
        super.onCreate()

        findViewById<RecyclerView>(R.id.recyclerView).linear().setup {

            addType<BaseData>(R.layout.item_writing_class_book)

            R.id.constraint.onClick {
                getModelOrNull<BaseData>()?.let {
                    back.invoke(it)
                }
                dismiss()
            }
        }.models=mList

    }
}
