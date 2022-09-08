package com.my.path.viewmodel.state


import android.text.Editable
import android.text.TextWatcher
import android.widget.CompoundButton
import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.callback.databind.BooleanObservableField
import me.guangnian.mvvm.callback.databind.StringObservableField

class LoginViewModel : BaseViewModel() {
    //手机号
    var userName = StringObservableField()
    //密码
    var password = StringObservableField()
    //登录按钮是否高亮
    var bgLoginBut = BooleanObservableField()
    //是否显示密码
    var isShowPwd = BooleanObservableField()

    //是否显示密码监听
    var passwordChecked  = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            isShowPwd.set(isChecked)
        }

    //登录按钮高亮监听
    var textChange = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {
            if(userName.get().isNotEmpty() && password.get().isNotEmpty()){
                bgLoginBut.set(true)
            }else{
                bgLoginBut.set(false)
            }
        }
    }
}