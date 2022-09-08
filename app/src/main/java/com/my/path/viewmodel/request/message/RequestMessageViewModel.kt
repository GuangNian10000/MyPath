package com.my.path.viewmodel.request.message

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
class RequestMessageViewModel : BaseViewModel() {
    var tipIndexResult = MutableLiveData<ResultState<TipIndex>>()
    var tipReadallResult = MutableLiveData<ResultState<BaseCode>>()
    var tipTicketResult = MutableLiveData<ListDataUiState<TipTicketData>>()
    var tipGiftResult = MutableLiveData<ListDataUiState<TipGiftData>>()
    var commentListResult = MutableLiveData<ListDataUiState<Post>>()
    var hongBaoListResult = MutableLiveData<ListDataUiState<HongBao>>()
    var commentListDataResult = MutableLiveData<CommentList>()

    var commentReplyResult = MutableLiveData<ListDataUiState<Post>>()
    var commentDetailsResult = MutableLiveData<ResultState<PostHead>>()
    var commentDataResult  = MutableLiveData<CommentList>()

    //评论详情
    fun commentDetails(_id:Int,cid:Int){
        requestNoCheck({HttpRequestCoroutine.commentDetails(_id,cid)},commentDetailsResult,false)
    }

    /**
     * 回复列表
     * */
    fun commentReply(_id:Int,pageNo:Int){
        val isRefresh =  pageNo ==1

        requestNoCheck({HttpRequestCoroutine.commentReply(_id,pageNo)},{
            //请求成功 自己拿到数据做业务需求操作
            var hasMore = it.posts.size==20
            val listDataUiState = ListDataUiState(
                isSuccess = true,
                isRefresh = isRefresh,
                isEmpty = it.posts.isEmpty(),
                hasMore = hasMore,
                isFirstEmpty = isRefresh && it.posts.isEmpty(),
                listData = it.posts )
            commentReplyResult.value = listDataUiState
            commentDataResult.value = it
        },{
            //请求失败 网络异常回调在这里
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<Post>()
                )
            commentReplyResult.value = listDataUiState
        })
    }

    //评论列表
    fun commentList(bid:Int,type:String,pageNo:Int){
        val isRefresh = pageNo==1
        requestNoCheck({HttpRequestCoroutine.commentList(bid,type,pageNo)},{
            it.posts.forEach { post->
                post.jnum = it.jnum
            }
            //请求成功 自己拿到数据做业务需求操作
            val listDataUiState = ListDataUiState(
                isSuccess = true,
                isRefresh = isRefresh,
                isEmpty = it.posts.isEmpty(),
                hasMore = true,
                isFirstEmpty = isRefresh && it.posts.isEmpty(),
                listData = it.posts )
            commentListResult.value = listDataUiState
            commentListDataResult.value = it
        },{
            //请求失败 网络异常回调在这里
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<Post>()
                )
            commentListResult.value = listDataUiState
        },pageNo==1)
    }

    //礼物列表
    fun tipGift(bid:Int,page:Int){
        val isRefresh =  page ==1
        requestNoCheck({HttpRequestCoroutine.tipGift(bid,page)},{
            val listDataUiState = ListDataUiState(
                isSuccess = true,
                isRefresh = page==1,
                isEmpty = it.isEmpty(),
                hasMore = true,
                isFirstEmpty = page==1 && it.isEmpty(),
                listData = it )
            tipGiftResult.value = listDataUiState
        },{
            //请求失败 网络异常回调在这里
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = page==1,
                    listData = arrayListOf<TipGiftData>()
                )
            tipGiftResult.value = listDataUiState
        },isRefresh)
    }

    //推荐票列表
    fun tipTicket(bid:Int,page:Int){
        if(0==bid){
            return
        }
        val isRefresh =  page ==1
        requestNoCheck({HttpRequestCoroutine.tipTicket(bid,page)},{
            //请求成功 自己拿到数据做业务需求操作
            val listDataUiState = ListDataUiState(
                isSuccess = true,
                isRefresh = page==1,
                isEmpty = it.isEmpty(),
                hasMore = true,
                isFirstEmpty = page==1 && it.isEmpty(),
                listData = it )
            tipTicketResult.value = listDataUiState
        },{
            //请求失败 网络异常回调在这里
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = page==1,
                    listData = arrayListOf<TipTicketData>()
                )
            tipTicketResult.value = listDataUiState
        },isRefresh)
    }

    //红包列表
    fun tipHongbao(bid:Int,page:Int){
        if(0==bid){
            return
        }
        val isRefresh =  page ==1
        requestNoCheck({HttpRequestCoroutine.tipHongbao(bid,page)},{
            //请求成功 自己拿到数据做业务需求操作
            val listDataUiState = ListDataUiState(
                isSuccess = true,
                isRefresh = page==1,
                isEmpty = it.isEmpty(),
                hasMore = true,
                isFirstEmpty = page==1 && it.isEmpty(),
                listData = it )
            hongBaoListResult.value = listDataUiState
        },{
            //请求失败 网络异常回调在这里
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = page==1,
                    listData = arrayListOf<HongBao>()
                )
            hongBaoListResult.value = listDataUiState
        },isRefresh)
    }

    //消息数量
    fun tipIndex(){
        requestNoCheck({HttpRequestCoroutine.tipIndex()},tipIndexResult,false)
    }

    //全部已读
    fun tipReadall(){
        requestNoCheck({HttpRequestCoroutine.tipReadall()},tipReadallResult,true)
    }
}