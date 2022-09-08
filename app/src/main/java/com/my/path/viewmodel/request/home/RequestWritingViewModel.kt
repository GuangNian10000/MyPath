package com.my.path.viewmodel.request.home

import androidx.lifecycle.MutableLiveData
import com.my.path.data.model.bean.*
import com.my.path.data.repository.request.HttpRequestCoroutine
import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.ext.requestNoCheck
import me.guangnian.mvvm.state.ResultState


class RequestWritingViewModel : BaseViewModel() {
    var authorIndexResult = MutableLiveData<ResultState<ArrayList<Novel>>>()
    var authorYclxResult = MutableLiveData<ResultState<ArrayList<Yc>>>()
    var authorSdfgResult = MutableLiveData<ResultState<ArrayList<Yc>>>()
    var authorXxsjResult = MutableLiveData<ResultState<ArrayList<Yc>>>()
    var authorCreateResult = MutableLiveData<ResultState<BaseCode>>()
    var authorEditBookResult = MutableLiveData<ResultState<BaseCode>>()
    var authorTagResult = MutableLiveData<ResultState<ArrayList<Tag>>>()
    var authorBookdelResult = MutableLiveData<ResultState<BaseCode>>()
    var authorZtResult = MutableLiveData<ResultState<ArrayList<Tag>>>()
    var authorCoverResult = MutableLiveData<ResultState<UploadAvatarFile>>()


    /**
     * 删除作品
     * */
    fun authorBookdel(bid:Int){
        requestNoCheck({HttpRequestCoroutine.authorBookdel(bid)},authorBookdelResult,true)
    }


    /**
     * 创建作品
     * */
    fun authorCreate(name:String,
                     intro:String,
                     yc:Int,
                     lx:Int,
                     xx:Int,
                     sj:Int,
                     sd:Int,
                     fg:Int){
        requestNoCheck({HttpRequestCoroutine.authorCreate(name,intro,yc,lx,xx,sj,sd,fg)},authorCreateResult,true)
    }

    /**
     * 修改作品
     * */
    fun authorEditBook(bid:Int,
                       intro:String,
                       remark:String,
                       yc:Int,
                       lx:Int,
                       xx:Int,
                       sj:Int,
                       sd:Int,
                       fg:Int,
                       zt:String,
                       tid:String){
        requestNoCheck({HttpRequestCoroutine.authorEditBook(bid,intro,remark,yc,lx,xx,sj,sd,fg,zt,tid)},authorEditBookResult,true)
    }

    /**
     * 状态
     * */
    fun authorZt(){
        requestNoCheck({HttpRequestCoroutine.authorZt()},authorZtResult,true)
    }

    /**
     * 标签
     * */
    fun authorTag(){
        requestNoCheck({HttpRequestCoroutine.authorTag()},authorTagResult,true)
    }

    /**
     * 作品列表
     * */
    fun authorIndex(){
        requestNoCheck({HttpRequestCoroutine.authorIndex()},authorIndexResult,false)
    }

    fun authorYclx(){
        requestNoCheck({HttpRequestCoroutine.authorYclx()},authorYclxResult,true)
    }

    fun authorSdfg(){
        requestNoCheck({HttpRequestCoroutine.authorSdfg()},authorSdfgResult,true)
    }

    fun authorXxsj(){
        requestNoCheck({HttpRequestCoroutine.authorXxsj()},authorXxsjResult,true)
    }

    /**
     * 上传照片
     * */
    fun authorCover(url:String,bid:Int,lang:String){
        requestNoCheck({HttpRequestCoroutine.authorCover(url,bid,lang)},authorCoverResult,true)
    }
}