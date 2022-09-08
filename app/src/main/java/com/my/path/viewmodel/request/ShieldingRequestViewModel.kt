package com.my.path.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.my.path.app.network.stateCallback.ListDataUiState

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
class ShieldingRequestViewModel : BaseViewModel() {
    var pageNo = 0
    var flowBlacklistResult  = MutableLiveData<ListDataUiState<Uinfo>>()
    var flowRemoveblacklistResult  = MutableLiveData<ResultState<BaseCode>>()
    var flowAddblacklistResult  = MutableLiveData<ResultState<BaseCode>>()
    /**
     * 屏蔽列表
     * */
    fun flowBlacklist(isRefresh: Boolean){
        if (isRefresh) {
            pageNo = 0
        }
        requestNoCheck({HttpRequestCoroutine.flowBlacklist(pageNo)},{
            //请求成功 自己拿到数据做业务需求操作
            pageNo++
            val listDataUiState = ListDataUiState(
                isSuccess = true,
                isRefresh = isRefresh,
                isEmpty = it.data.isEmpty(),
                hasMore = true,
                isFirstEmpty = isRefresh && it.data.isEmpty(),
                listData = it.data )
            flowBlacklistResult.value = listDataUiState
        },{
            //请求失败 网络异常回调在这里
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<Uinfo>()
                )
            flowBlacklistResult.value = listDataUiState
        })
    }

    /**
     * 添加屏蔽
     * */
    fun flowRemoveblacklist(position:Int,did:Int){
        requestNoCheck({ HttpRequestCoroutine.flowRemoveblacklist(position,did)},flowRemoveblacklistResult,true)
    }
    /**
     * 取消屏蔽
     * */
    fun flowAddblacklist(did:Int){
        requestNoCheck({ HttpRequestCoroutine.flowAddblacklist(did)},flowAddblacklistResult,true)
    }
}