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
 * @description　:码字日历的请求ViewModel
 */
class RequestCalendarViewModel : BaseViewModel() {

    var meUpdateResult = MutableLiveData<ResultState<CalendarCode>>()
    var meGetbookResult = MutableLiveData<ResultState<MeGetBook>>()

    //码字日历
    fun meUpdate(bid:String,year:String,month:String){
        requestNoCheck({ HttpRequestCoroutine.meUpdate(bid,year,month)},meUpdateResult,false)
    }

    //获取作品
    fun meGetbook(){
        requestNoCheck({ HttpRequestCoroutine.meGetbook()},meGetbookResult,false)
    }
}