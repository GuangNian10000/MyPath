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
 * @description　:
 */
class RequestMeViewModel : BaseViewModel() {
    var meGetInFoResult = MutableLiveData<ResultState<UserData>>()
    var meGetbookResult = MutableLiveData<ResultState<MeGetBook>>()
    var meSaveauthorResult = MutableLiveData<ResultState<BaseCode>>()

    var meSaveinfoResult = MutableLiveData<ResultState<BaseCode>>()
    var meUploadResult = MutableLiveData<ResultState<UploadAvatarFile>>()

    //获取作品
    fun meGetbook(){
        requestNoCheck({ HttpRequestCoroutine.meGetbook()},meGetbookResult,false)
    }

    /**
     * 获取个人信息
     * */
    fun meGetInFo(){
        requestNoCheck({HttpRequestCoroutine.meGetInFo()},meGetInFoResult,false)
    }

    /**
     * 修改个人信息
     * */
    fun meSaveinfo(meGetInFo:UserData){
        requestNoCheck({HttpRequestCoroutine.meSaveinfo(meGetInFo)},meSaveinfoResult,true)
    }

    /**
     * 修改头像
     * */
    fun meUploadAvatarfile(picture:String){
        requestNoCheck({HttpRequestCoroutine.meUploadAvatarfile(picture)},meUploadResult,true)
    }

    /**
     * 修改笔名
     * */
    fun meSaveauthor(author:String){
        requestNoCheck({HttpRequestCoroutine.meSaveauthor(author)},meSaveauthorResult,true)
    }
}