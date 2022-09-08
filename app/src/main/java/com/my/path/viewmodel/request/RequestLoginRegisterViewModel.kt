package com.my.path.viewmodel.request

import androidx.lifecycle.MutableLiveData

import com.my.path.data.model.bean.BaseCode
import com.my.path.data.model.bean.CountryEntity
import com.my.path.data.model.bean.LoginInfo
import com.my.path.data.repository.request.HttpRequestCoroutine
import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.ext.requestNoCheck
import me.guangnian.mvvm.state.ResultState


/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　:登录注册的请求ViewModel
 */
class RequestLoginRegisterViewModel : BaseViewModel() {

    //方式1  自动脱壳过滤处理请求结果，判断结果是否成功
    var loginResult = MutableLiveData<ResultState<LoginInfo>>()

    //方式2  不用框架帮脱壳，判断结果是否成功
    //var loginResult2 = MutableLiveData<ResultState<ApiResponse<UserInfo>>>()
    var codeResult = MutableLiveData<ResultState<CountryEntity>>()
    var sendCode = MutableLiveData<ResultState<BaseCode>>()
    var registerEmailResult = MutableLiveData<ResultState<LoginInfo>>()
    var sendEmailCodeResult = MutableLiveData<ResultState<BaseCode>>()
    var emailCodeResult = MutableLiveData<ResultState<BaseCode>>()
    var sendVoiceCode = MutableLiveData<ResultState<BaseCode>>()

    /**
     * 登录
     * */
    fun loginReq(username: String, password: String) {
        requestNoCheck({HttpRequestCoroutine.login(username,password)},loginResult,true)
        //1.这种是在 Activity/Fragment的监听回调中拿到已脱壳的数据（项目有基类的可以用）
//       request(
//            { apiService.login(username, password)}//请求体
//            , loginResult,//请求的返回结果，请求成功与否都会改变该值，在Activity或fragment中监听回调结果，具体可看loginActivity中的回调
//            true,//是否显示等待框，，默认false不显示 可以默认不传
//            "正在登录中..."//等待框内容，可以默认不填请求网络中...
//        )
        //2.这种是在Activity/Fragment中的监听拿到未脱壳的数据，你可以自己根据code做业务需求操作（项目没有基类的可以用）
        /**/
    //   requestNoCheck({HttpRequestCoroutine.login(username,password)},loginResult,true)
        //3. 这种是直接在当前ViewModel中就拿到了脱壳数据数据，做一层封装再给Activity/Fragment，如果 （项目有基类的可以用）
         /*request({apiService.login(username, password)},{
             //请求成功 已自动处理了 请求结果是否正常
         },{
             //请求失败 网络异常，或者请求结果码错误都会回调在这里
         })*/

        //4.这种是直接在当前ViewModel中就拿到了未脱壳数据数据，（项目没有基类的可以用）
//        requestNoCheck({HttpRequestCoroutine.login(username,password)},{
//            //请求成功 自己拿到数据做业务需求操作
//            if(it.st==200){
//                //结果正确
//                loginResult.paresResult(it)
//            }else{
//                //结果错误
//            }
//        },{
//            //请求失败 网络异常回调在这里
//        })
    }



    /**
     * 邮箱注册
     * */
    fun registerEmail(email: String, code: String,password:String,nick:String) {
        requestNoCheck(
            { HttpRequestCoroutine.registerEmail(email, code, password,nick) }
            , registerEmailResult,
            true,
            "正在注册中..."
        )
    }

    /**
     * 验证码登录
     * */
    fun loginCode(country:String,phone:String,code:String) {
        requestNoCheck(
            { HttpRequestCoroutine.loginCode(country, phone,code) }
            , loginResult,
            true
        )
    }
    /**
     * 获取国别码
     * */
    fun ip2country(){
        requestNoCheck({HttpRequestCoroutine.ip2country()},codeResult,true)
    }

    /**
     * 发送验证码
     * */
    fun registerSendcode(country:String,phone:String){
        requestNoCheck({HttpRequestCoroutine.registerSendcode(country,phone)},sendCode,true)
    }

    fun loginSendcode(country:String,phone:String){
        requestNoCheck({HttpRequestCoroutine.loginSendcode(country,phone)},sendCode,true)
    }

    /**
     * 发送邮箱验证码
     * */
    fun registerSendemail(phone:String){
        requestNoCheck({HttpRequestCoroutine.registerSendemail(phone)},sendEmailCodeResult,true)
    }

    /**
     * 发送语音验证码
     * */
    fun registerSendvoice(country:String,phone:String){
        requestNoCheck({HttpRequestCoroutine.registerSendvoice(country,phone)},sendCode,true)
    }


    fun sendvoice(country:String,phone:String){
        requestNoCheck({HttpRequestCoroutine.sendvoice(country,phone)},sendVoiceCode,true)
    }


    /**
     * 忘记密码
     * */
    fun editpass(country:String,phone:String,code:String,password:String,repassword:String){
        requestNoCheck({HttpRequestCoroutine.editpass(country,phone,code,password,repassword)},loginResult,true)
    }

    fun registerBind(country: String, phone: String,code:String){
        requestNoCheck({HttpRequestCoroutine.registerBind(country,phone,code)},sendEmailCodeResult,true)
    }

    fun registerBindemail(email: String,code:String){
        requestNoCheck({HttpRequestCoroutine.registerBindemail(email,code)},emailCodeResult,true)
    }

}