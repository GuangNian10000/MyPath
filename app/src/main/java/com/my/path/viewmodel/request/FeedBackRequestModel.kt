package com.my.path.viewmodel.request

import androidx.lifecycle.MutableLiveData

import com.my.path.data.model.bean.FeedBackAdd
import com.my.path.data.model.bean.Picture
import com.my.path.data.repository.request.HttpRequestCoroutine
import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.ext.requestNoCheck
import me.guangnian.mvvm.state.ResultState


/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　:意见反馈的ViewModel
 */
class FeedBackRequestModel : BaseViewModel() {
    var feedbackAddResult  = MutableLiveData<ResultState<FeedBackAdd>>()

    var feedbackUpimgResult  = MutableLiveData<ResultState<MutableList<Picture>>>()

    /**
     * 提交
     * */
    fun feedbackAdd(content:String,urls:String){
        requestNoCheck({ HttpRequestCoroutine.feedbackAdd(content,urls)},feedbackAddResult,true)
    }

    /**
     * 上传图片
     * */
    fun feedbackUpimg(listPicture:MutableList<Picture>){
        requestNoCheck({ HttpRequestCoroutine.feedbackUpimg(listPicture)},feedbackUpimgResult,true)
    }
}