package com.my.path.app.weight.loadCallBack

import android.content.Context
import android.view.View
import com.my.path.R
import com.my.path.app.eventViewModel
import com.kingja.loadsir.callback.Callback


class ErrorPenNameCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_error
    }


    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        eventViewModel.inPenName.value = true
        return true
    }
}