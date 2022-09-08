package com.my.path.ui.dialog

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import com.my.path.R
import com.my.path.data.model.bean.Tag
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.lxj.xpopup.core.BottomPopupView

class ZtListDialog(var mActivity: Activity, val mList : ArrayList<Tag>, val back: (Tag)->Unit) : BottomPopupView(mActivity){

    override fun getImplLayoutId():Int = R.layout.popup_window_zt

    override fun onCreate() {
        super.onCreate()

        findViewById<RecyclerView>(R.id.recyclerView).linear().setup {

            addType<Tag>(R.layout.item_writing_class_zt)

            R.id.tvText.onClick {
                getModelOrNull<Tag>()?.let {
                    back.invoke(it)
                }
                dismiss()
            }
        }.models=mList

    }
}
