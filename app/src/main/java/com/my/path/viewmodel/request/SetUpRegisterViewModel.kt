package com.my.path.viewmodel.request

import androidx.lifecycle.MutableLiveData

import com.my.path.data.model.bean.*
import com.my.path.data.repository.request.HttpRequestCoroutine
import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.ext.requestNoCheck
import me.guangnian.mvvm.state.ResultState

/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　:设置的请求ViewModel
 */
class SetUpRegisterViewModel : BaseViewModel() {
    var logoutResult = MutableLiveData<ResultState<BaseCode>>()

    /**
     * 退出登录
     * */
    fun logout(){
        requestNoCheck({HttpRequestCoroutine.logout()},logoutResult,true)
    }
}