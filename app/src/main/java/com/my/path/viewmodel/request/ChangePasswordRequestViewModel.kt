package com.my.path.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.my.path.data.model.bean.BaseCode
import com.my.path.data.repository.request.HttpRequestCoroutine
import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.ext.requestNoCheck
import me.guangnian.mvvm.state.ResultState


/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 *
    var meEditpassResult = MutableLiveData<ResultState<BaseCode>>()
    fun meEditpass(oldpass:String,newpass:String){
    requestNoCheck({HttpRequestCoroutine.meEditpass(oldpass,newpass)},meEditpassResult,true)
    }
 * @description　:登录注册的请求ViewModel
 */
class ChangePasswordRequestViewModel : BaseViewModel() {
    var meEditpassResult = MutableLiveData<ResultState<BaseCode>>()
    fun meEditpass(oldpass:String,newpass:String){
        requestNoCheck({HttpRequestCoroutine.meEditpass(oldpass,newpass)},meEditpassResult,true)
    }

}