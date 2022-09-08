package com.my.path.viewmodel.state

import android.text.Editable
import android.text.TextWatcher
import android.util.ArrayMap

import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.callback.databind.BooleanObservableField
import me.guangnian.mvvm.callback.databind.StringObservableField


/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　:登录注册的ViewModel
 */
class ForgetPwdViewModel : BaseViewModel() {
    //用户名（注册）
    var phoneReg = StringObservableField()
    //密码(注册)
    var passwordReg = StringObservableField()
    //国别码
    var countryCode = StringObservableField("+86")

    //手机验证码
    var phoneCode = StringObservableField()

    var bgMap:MutableMap<String,Boolean> = ArrayMap()
    var bgBut = BooleanObservableField()
    //监听账号输入
    val simpleTextWatcherReg1 = object :SimpleTextWatcher(){
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            bgMap["simpleTextWatcherReg"] = s.isNotEmpty()

            bgBut.set(bgMap.filter {
                it.value
            }.size==3)
        }
    }

    //监听密码输入
    val simpleTextWatcherReg2 = object :SimpleTextWatcher(){
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            bgMap["simpleTextWatcherReg"] = s.isNotEmpty()

            bgBut.set(bgMap.filter {
                it.value
            }.size==3)
        }
    }

    //监听验证码输入
    val simpleTextWatcherReg3 = object :SimpleTextWatcher(){
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            bgMap["simpleTextWatcherReg"] = s.isNotEmpty()

            bgBut.set(bgMap.filter {
                it.value
            }.size==3)
        }
    }

    abstract class SimpleTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {}
    }
}