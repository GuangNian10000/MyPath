package com.my.path.viewmodel.request.message

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
class RequestCommentActionViewModel : BaseViewModel() {
    var replyPostResult  = MutableLiveData<ResultState<ReplyPost>>()
    var commentLikedResult  = MutableLiveData<ResultState<BaseCode>>()
    var commentReplyLikedResult  = MutableLiveData<ResultState<BaseCode>>()

    var commentTopResult  = MutableLiveData<ResultState<BaseCode>>()
    var commentGoodResult  = MutableLiveData<ResultState<BaseCode>>()
    var commentDelResult  = MutableLiveData<ResultState<BaseCode>>()

    /**
     * 评论
     * */
    fun commentReplyPost(_id:Int,content:String,file:String){
        requestNoCheck({ HttpRequestCoroutine.commentReplyPost(_id,content,file)},replyPostResult,true)
    }

    /**
     * 评论点赞
     * */
    fun commentLiked(_id:Int,op:Int){
        requestNoCheck({ HttpRequestCoroutine.commentLiked(_id,op)},commentLikedResult,true)
    }

    /**
     * 评论点赞
     * */
    fun commentReplyLiked(_id:Int,op:Int){
        requestNoCheck({ HttpRequestCoroutine.commentReplyLiked(_id,op)},commentReplyLikedResult,true)
    }

    /**
     * 评论删除
     * */
    fun commentDel(_id:Int,pos:Int){
        requestNoCheck({ HttpRequestCoroutine.commentDel(_id,pos)},commentDelResult,true)
    }

    /**
     * 设置精华
     * */
    fun commentGood(position:Int,state:Int,_id:Int){
        requestNoCheck({ HttpRequestCoroutine.commentGood(position,state,_id)},commentGoodResult,true)
    }

    /**
     * 置顶
     * */
    fun commentTop(_id:Int){
        requestNoCheck({ HttpRequestCoroutine.commentTop(_id)},commentTopResult,true)
    }
}