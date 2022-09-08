package com.my.path.viewmodel.request.chapter

import androidx.lifecycle.MutableLiveData
import com.my.path.app.eventViewModel
import com.my.path.data.model.bean.*
import com.my.path.data.repository.request.HttpRequestCoroutine
import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.ext.requestNoCheck
import me.guangnian.mvvm.state.ResultState


class RequestChapterDataViewModel : BaseViewModel() {

    var authorChapterlistResult = MutableLiveData<ResultState<ChapterList>>()

    var authorEditchapterenameResult = MutableLiveData<BaseCode>()

    var getArticleContentResult = MutableLiveData<ResultState<ChapterContent>>()

    var authorChapterDelResult  = MutableLiveData<ResultState<BaseCode>>()
    var authorChapterPublishResult  = MutableLiveData<ResultState<BaseCode>>()

    var authorChaptermoveupResult  = MutableLiveData<ResultState<BaseCode>>()
    var authorChaptermovedownResult  = MutableLiveData<ResultState<BaseCode>>()

    var authorChaptertimingResult  = MutableLiveData<ResultState<BaseCode>>()

    var authorSavechapterResult = MutableLiveData<ResultState<SaveChapter>>()

    /**
     * 修改作品
     * */
    fun authorSavechapter(bid:Int,
                          chaptername:String,
                          content:String,
                          say:String,
                          publish:Int,volume:String, cid:String=""){
        requestNoCheck({HttpRequestCoroutine.authorSavechapter(bid,chaptername,content,say,publish,volume,cid)},authorSavechapterResult,true)
    }


    fun authorChapterlist(bid:Int){
        requestNoCheck({HttpRequestCoroutine.authorChapterlist(bid)},authorChapterlistResult,false)
    }

    fun authorChapterDraftlist(bid:Int){
        requestNoCheck({HttpRequestCoroutine.authorChapterDraftlist(bid)},authorChapterlistResult,false)
    }

    /**
     * 修改章节
     * */
    fun authorEditchapterename(position:Int,bid:Int,cid:Int,chaptername:String){
        requestNoCheck({ HttpRequestCoroutine.authorEditchapterename(position,bid,cid,chaptername)},{
            authorEditchapterenameResult.value = it
        })
    }


    /**
     * 获取文章内容
     * */
    fun getArticleContent(bid:Int,
                          cid:Int){
        requestNoCheck({HttpRequestCoroutine.authorGetchapter(bid,cid)},getArticleContentResult,false)
    }

    /**
     * 删除章节
     * */
    fun authorChapterDel(bid:Int,cid:Int,position:Int){
        requestNoCheck({ HttpRequestCoroutine.authorChapterDel(bid,cid,position)},authorChapterDelResult,true)
    }

    /**
     * 公开章节
     * */
    fun authorChapterPublish(bid:Int,cid:Int,position:Int){
        requestNoCheck({ HttpRequestCoroutine.authorChapterPublish(bid,cid,position)},authorChapterPublishResult,true)
    }

    /**
     * 上移
     * */
    fun authorChaptermoveup(bid:Int,cid:Int,position:Int){

        requestNoCheck({ HttpRequestCoroutine.authorChaptermoveup(bid,cid,if(eventViewModel.isReverseOrder.value == true) 1 else 0,position)},authorChaptermoveupResult,true)
    }

    /**
     * 定时
     * */
    fun authorChaptertiming(position:Int,bid:Int,cid:Int,timing:String){
        requestNoCheck({ HttpRequestCoroutine.authorChaptertiming(position,bid,cid,timing)},authorChaptertimingResult,true)
    }


    /**
     * 下移
     * */
    fun authorChaptermovedown(bid:Int,cid:Int,position:Int){
        requestNoCheck({ HttpRequestCoroutine.authorChaptermovedown(bid,cid,if(eventViewModel.isReverseOrder.value == true) 1 else 0,position)},authorChaptermovedownResult,true)
    }
}