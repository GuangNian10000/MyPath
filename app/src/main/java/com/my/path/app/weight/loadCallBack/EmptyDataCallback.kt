package com.my.path.app.weight.loadCallBack


import com.my.path.R
import com.kingja.loadsir.callback.Callback


class EmptyDataCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_empty_home_data
    }
}