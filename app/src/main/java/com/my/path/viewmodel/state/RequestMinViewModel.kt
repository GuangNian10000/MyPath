package com.my.path.viewmodel.state

import androidx.lifecycle.MutableLiveData

import com.my.path.data.model.bean.*
import com.my.path.data.repository.request.HttpRequestCoroutine
import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.ext.requestNoCheck
import me.guangnian.mvvm.state.ResultState

/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　:
 */
class RequestMinViewModel : BaseViewModel() {
    var meGetInFoResult = MutableLiveData<ResultState<UserData>>()

    /**
     * 获取个人信息
     * */
    fun meGetInFo(){
        requestNoCheck({HttpRequestCoroutine.meGetInFo()},meGetInFoResult,true)
    }
}