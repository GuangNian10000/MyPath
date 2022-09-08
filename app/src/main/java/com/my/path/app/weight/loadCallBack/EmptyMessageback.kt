package com.my.path.app.weight.loadCallBack


import com.my.path.R
import com.kingja.loadsir.callback.Callback


class EmptyMessageback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.view_data_message_null
    }
}