package com.my.path.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.my.path.app.util.CacheUtil.setLoginInfo
import com.my.path.data.model.bean.BaseCode
import com.my.path.data.model.bean.CountryEntity
import com.my.path.data.model.bean.LoginInfo
import com.my.path.data.repository.request.HttpRequestCoroutine
import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.ext.requestNoCheck
import me.guangnian.mvvm.state.ResultState


class RequestLoginViewModel : BaseViewModel() {

    var loginResult = MutableLiveData<LoginInfo>()
    var codeResult = MutableLiveData<ResultState<CountryEntity>>()
    var sendCode = MutableLiveData<ResultState<BaseCode>>()
    var sendVoiceCode = MutableLiveData<ResultState<BaseCode>>()
    var registerOnekeyResult = MutableLiveData<ResultState<LoginInfo>>()

    fun login(username: String, password: String) {
        requestNoCheck({HttpRequestCoroutine.login(username,password)},{
            setLoginInfo(it)
            loginResult.value = it
        },{
        },true)
    }

    fun loginCode(country:String,phone:String,code:String) {
        requestNoCheck({HttpRequestCoroutine.loginCode(country,phone,code)},{
            setLoginInfo(it)
            loginResult.value = it
        },{
        },true)
    }


    fun register(country: String, phone: String, code: String, password: String, nick: String="") {
        requestNoCheck({HttpRequestCoroutine.register(country, phone, code,password,nick)},{
            setLoginInfo(it)
            loginResult.value = it
        },{
        },true)
    }

    /**
     * 邮箱注册
     * */
    fun registerEmail( phone: String, code: String, password: String, nick: String="") {
        requestNoCheck({HttpRequestCoroutine.registerEmail(phone, code,password,nick)},{
            setLoginInfo(it)
            loginResult.value = it
        },{
        },true)
    }

    /**
     * 邮箱找回密码
     * */
    fun editpassEmail( phone: String, code: String, password: String) {
        requestNoCheck({HttpRequestCoroutine.editpassEmail(phone, code,password,password)},{
            setLoginInfo(it)
            loginResult.value = it
        },{
        },true)
    }

    /**
     * 忘记密码
     * 手机号找回
     * */
    fun editpass(country: String, phone: String, code: String, password: String) {
        requestNoCheck({HttpRequestCoroutine.editpass(country, phone, code,password,password)},{
            setLoginInfo(it)
            loginResult.value = it
        },{
        },true)
    }

    /**
     * 获取国别码
     * */
    fun ip2country(){
        requestNoCheck({HttpRequestCoroutine.ip2country()},codeResult,true)
    }

    /**
     * 发送验证码
     * 邮箱找回
     * */
    fun sendbindemail(phone:String){
        requestNoCheck({HttpRequestCoroutine.sendbindemail(phone)},sendCode,true)
    }

    /**
     * 发送验证码
     * 邮箱注册
     * */
    fun registerSendemail(phone:String){
        requestNoCheck({HttpRequestCoroutine.registerSendemail(phone)},sendCode,true)
    }


    fun registerSendcode(country:String,phone:String){
        requestNoCheck({HttpRequestCoroutine.registerSendcode(country,phone)},sendCode,true)
    }

    /**
     * 发送验证码
     * 登录、忘记密码
     * */
    fun loginSendcode(country:String,phone:String){
        requestNoCheck({HttpRequestCoroutine.loginSendcode(country,phone)},sendCode,true)
    }


    /**
     * 发送语音验证码
     * 注册、绑定
     * */
    fun registerSendvoice(country:String,phone:String){
        requestNoCheck({HttpRequestCoroutine.registerSendvoice(country,phone)},sendVoiceCode,true)
    }

    /**
     * 发送语音验证码
     * 忘记密码
     * */
    fun sendvoice(country:String,phone:String){
        requestNoCheck({HttpRequestCoroutine.sendvoice(country,phone)},sendVoiceCode,true)
    }


    fun registerOnekey(tk:String){
        requestNoCheck({HttpRequestCoroutine.registerOnekey(tk)},registerOnekeyResult,true)
    }
}