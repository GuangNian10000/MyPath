package com.my.path.viewmodel.state

import android.util.ArrayMap

import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.callback.databind.BooleanObservableField
import me.guangnian.mvvm.callback.databind.StringObservableField


/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　:登录注册的ViewModel
 */
class ChangePasswordViewModel : BaseViewModel() {
    var pawd1 = StringObservableField()
    var pawd2 = StringObservableField()

    var bgMap:MutableMap<String,Boolean> = ArrayMap()
    var bgBut = BooleanObservableField()

    //监听账号输入（注册）
    val simpleTextWatcher1 = object : LoginRegisterViewModel.SimpleTextWatcher(){
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            bgMap["simpleTextWatcherReg1"] = s.isNotEmpty()

            bgBut.set(bgMap.filter {
                it.value
            }.size==2)
        }
    }

    //监听密码输入（注册）
    val simpleTextWatcher2 = object : LoginRegisterViewModel.SimpleTextWatcher(){
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            bgMap["simpleTextWatcherReg2"] = s.isNotEmpty()

            bgBut.set(bgMap.filter {
                it.value
            }.size==2)
        }
    }
}