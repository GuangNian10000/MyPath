package com.my.path.viewmodel.state

import android.text.Editable
import android.text.TextWatcher
import android.util.ArrayMap
import android.view.View
import androidx.databinding.ObservableInt
import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.callback.databind.BooleanObservableField
import me.guangnian.mvvm.callback.databind.StringObservableField


/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　:登录注册的ViewModel
 */
class LoginRegisterViewModel : BaseViewModel() {

    //用户名（登录）
    var username = StringObservableField()

    //用户名（注册）
    var phoneReg = StringObservableField()

    //用户名（验证码登录）
    var phoneUserCode = StringObservableField()

    //用户名（邮箱注册）
    var email = StringObservableField()

    //密码(登录)
    var password = StringObservableField()

    //密码(验证码登录)
    var passwordReg = StringObservableField()

    //密码(注册)
    var passwordCode = StringObservableField()

    //用户名（邮箱注册）
    var emailPassword = StringObservableField()
    //用户名（邮箱注册）
    var emailCode = StringObservableField()

    var password2 = StringObservableField()

    //是否显示明文密码（登录注册界面）
    var isShowPwd = BooleanObservableField()

    var isShowPwd2 = BooleanObservableField()

    //国别码
    var countryCode = StringObservableField()
    var countryName = StringObservableField()

    //手机验证码
    var phoneCode = StringObservableField()

    //用户名密码是否为空(登录)
    var bgLoginMap:MutableMap<String,Boolean> = ArrayMap()
    var bgLoginBut = BooleanObservableField()

    //用户名密码是否为空(注册)
    var bgRegMap:MutableMap<String,Boolean> = ArrayMap()
    var bgRegBut = BooleanObservableField()

    //用户名密码是否为空(验证码登录)
    var bgCodeMap:MutableMap<String,Boolean> = ArrayMap()
    var bgCodeBut = BooleanObservableField()

    //用户名密码是否为空(email注册)
    var bgEmailMap:MutableMap<String,Boolean> = ArrayMap()
    var bgEmailBut = BooleanObservableField()

    //用户服务协议
    var isCheckbox = BooleanObservableField()

    //用户服务协议
    var isPhoneCheckbox = BooleanObservableField()

    var isRegCheckbox = BooleanObservableField()

    //用户名清除按钮是否显示   不要在 xml 中写逻辑 所以逻辑判断放在这里
    var clearVisible = object :ObservableInt(username){
        override fun get(): Int {
            return if(username.get().isEmpty()){
                View.GONE
            }else{
                View.VISIBLE
            }
        }
    }

    //密码显示按钮是否显示   不要在 xml 中写逻辑 所以逻辑判断放在这里
    var passwordVisible = object :ObservableInt(password){
        override fun get(): Int {
            return if(password.get().isEmpty()){
                View.GONE
            }else{
                View.VISIBLE
            }
        }
    }

    //密码显示按钮是否显示   不要在 xml 中写逻辑 所以逻辑判断放在这里
    var passwordVisible2 = object :ObservableInt(password2){
        override fun get(): Int {
            return if(password2.get().isEmpty()){
                View.GONE
            }else{
                View.VISIBLE
            }
        }
    }

    //监听账号输入（登录）
    val simpleTextWatcher1 = object :SimpleTextWatcher(){
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            bgLoginMap["simpleTextWatcher1"] = s.isNotEmpty()

            bgLoginBut.set(bgLoginMap.filter {
                it.value
            }.size==2)
        }
    }

    //监听密码输入（密码）
    val simpleTextWatcher2 = object :SimpleTextWatcher(){
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            bgLoginMap["simpleTextWatcher2"] = s.isNotEmpty()

            bgLoginBut.set(bgLoginMap.filter {
                it.value
            }.size==2)
        }
    }

    //监听账号输入（注册）
    val simpleTextWatcherReg1 = object :SimpleTextWatcher(){
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            bgRegMap["simpleTextWatcherReg1"] = s.isNotEmpty()

            bgRegBut.set(bgRegMap.filter {
                it.value
            }.size==3)
        }
    }

    //监听密码输入（注册）
    val simpleTextWatcherReg2 = object :SimpleTextWatcher(){
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            bgRegMap["simpleTextWatcherReg2"] = s.isNotEmpty()

            bgLoginBut.set(bgRegMap.filter {
                it.value
            }.size==3)
        }
    }

    //监听验证码输入（注册）
    val simpleTextWatcherReg3 = object :SimpleTextWatcher(){
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            bgRegMap["simpleTextWatcherReg3"] = s.isNotEmpty()

            bgRegBut.set(bgRegMap.filter {
                it.value
            }.size==3)
        }
    }


    //监听账号输入（注册）
    val simpleTextWatcherCode1 = object :SimpleTextWatcher(){
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            bgCodeMap["simpleTextWatcherCode1"] = s.isNotEmpty()

            bgCodeBut.set(bgCodeMap.filter {
                it.value
            }.size==2)
        }
    }

    //监听密码输入（注册）
    val simpleTextWatcherCode2 = object :SimpleTextWatcher(){
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            bgCodeMap["simpleTextWatcherCode2"] = s.isNotEmpty()

            bgCodeBut.set(bgCodeMap.filter {
                it.value
            }.size==2)
        }
    }

    //监听账号输入（注册）
    val simpleTextWatcherEmail1 = object :SimpleTextWatcher(){
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            bgEmailMap["simpleTextWatcherEmail1"] = s.isNotEmpty()

            bgEmailBut.set(bgEmailMap.filter {
                it.value
            }.size==3)
        }
    }

    //监听密码输入（注册）
    val simpleTextWatcherEmail2 = object :SimpleTextWatcher(){
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            bgEmailMap["simpleTextWatcherEmail2"] = s.isNotEmpty()

            bgEmailBut.set(bgEmailMap.filter {
                it.value
            }.size==3)
        }
    }

    //监听验证码输入（注册）
    val simpleTextWatcherEmail3 = object :SimpleTextWatcher(){
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            bgEmailMap["simpleTextWatcherEmail3"] = s.isNotEmpty()

            bgEmailBut.set(bgEmailMap.filter {
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