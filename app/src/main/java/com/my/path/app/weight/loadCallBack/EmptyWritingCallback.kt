package com.my.path.app.weight.loadCallBack


import android.content.Context
import android.view.View
import com.my.path.R
import com.my.path.app.eventViewModel
import com.kingja.loadsir.callback.Callback


class EmptyWritingCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.view_home_writing_null
    }

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        eventViewModel.inAddWriting.value = true
        return true
    }
}