package com.my.path.viewmodel.request.data

import androidx.lifecycle.MutableLiveData
import com.my.path.app.network.stateCallback.ListDataUiState
import com.my.path.data.model.bean.*
import com.my.path.data.repository.request.HttpRequestCoroutine
import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.ext.requestNoCheck
import me.guangnian.mvvm.state.ResultState


class RequestDataViewModel : BaseViewModel() {
    var pageNo = 0
    var dataBaseResult = MutableLiveData<ResultState<DataBase>>()
    var dataSurveyResult = MutableLiveData<ResultState<DataSurvey>>()
    var dataSubscribeResult = MutableLiveData<ResultState<DataSub>>()
    var dataGiftResult = MutableLiveData<ResultState<DataGift>>()
    var dataSubscribeDetailResult = MutableLiveData<ListDataUiState<DataSubscribe>>()

    var dataIncomeResult = MutableLiveData<ResultState<DataIncome>>()
    var dataPaymentResult = MutableLiveData<ResultState<Payment>>()

    fun dataBase(){
        requestNoCheck({HttpRequestCoroutine.dataBase()},dataBaseResult,false)
    }

    fun dataSurvey(bid:Int){
        requestNoCheck({HttpRequestCoroutine.dataSurvey(bid)},dataSurveyResult,true)
    }

    //订阅详情
    fun dataSubscribeDetail(bid:Int,isRefresh: Boolean){
        if (isRefresh) {
            pageNo = 0
        }
        requestNoCheck({HttpRequestCoroutine.dataSubscribeDetail(bid,pageNo)},{
            //请求成功 自己拿到数据做业务需求操作
            pageNo++
            val listDataUiState = ListDataUiState(
                isSuccess = true,
                isRefresh = isRefresh,
                isEmpty = it.isEmpty(),
                hasMore = true,
                isFirstEmpty = isRefresh && it.isEmpty(),
                listData = it )
            dataSubscribeDetailResult.value = listDataUiState
        },{
            //请求失败 网络异常回调在这里
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<DataSubscribe>()
                )
            dataSubscribeDetailResult.value = listDataUiState
        })
    }

    //订阅概况
    fun dataSubscribe(bid:Int){
        requestNoCheck({HttpRequestCoroutine.dataSubscribe(bid)},dataSubscribeResult,true)
    }

    //礼物收益
    fun dataGift(bid:Int){
        requestNoCheck({HttpRequestCoroutine.dataGift(bid)},dataGiftResult,true)
    }

    //礼物收益
    fun dataIncome(bid:Int){
        requestNoCheck({HttpRequestCoroutine.dataIncome(bid)},dataIncomeResult,true)
    }  //

    //稿费情况
    fun dataPayment(bid:Int){
        requestNoCheck({HttpRequestCoroutine.dataPayment(bid)},dataPaymentResult,true)
    }  //
}