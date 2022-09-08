package com.my.path.viewmodel.state


import android.widget.CompoundButton
import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.callback.databind.BooleanObservableField
import me.guangnian.mvvm.callback.databind.StringObservableField

class CodeViewModel : BaseViewModel() {
    // 验证码
    var verificationCode =  StringObservableField()
    // 密码
    var password = StringObservableField()
    //是否显示密码
    var isShowPwd = BooleanObservableField()

    //是否显示密码监听
    var passwordChecked  = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        isShowPwd.set(isChecked)
    }

}