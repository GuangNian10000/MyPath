package com.my.path.data.model.bean

import android.graphics.drawable.Drawable
import com.my.path.R
import com.my.path.app.ext.getBackgroundExt
import com.my.path.app.ext.getColor

/**
 * @author GuangNian
 * @description:
 * @date : 2022/2/25 4:54 下午
 */
data class CommentList(
    var count: String="",
    var jnum: Int=0,
    var manage: Int=0,
    var posts: ArrayList<Post> = ArrayList(),
    var st: Int=0
)

data class Post(
    var jnum: Int=0,
    var _id: Int=0,
    var author: Uinfo= Uinfo(),
    var bid: Int=0,
    var bookname: String="",
    var chapterid: String="",
    var chaptername: String="",
    var content: String="",
    var img: String="",
    var isgood: Int=0,
    var isliked: Int=0,
    var istop: Int=0,
    var likecount: Int=0,
    var reply: ArrayList<Reply> = ArrayList(),
    var replycount: Int=0,
    var type: String="",
    var updated: String=""
){
    //置顶
    fun isTopBg(): Drawable? = if(istop==0){
        getBackgroundExt( R.drawable.bg_comment_zhiding_off)

        }else{
        getBackgroundExt( R.drawable.bg_comment_zhiding_on)
        }

    fun isTopText(): String = if(istop==0){
        "置顶"
    }else{
        "取消置顶"
    }

    fun isTopTextColor(): Int = if(istop==0){
        getColor(R.color.base_color)
    }else{
        getColor(R.color.qulusidalse)
    }


    //加精
    fun isGoodBg(): Drawable? = if(isgood==0){
        getBackgroundExt( R.drawable.bg_comment_zhiding_off)
    }else{
        getBackgroundExt( R.drawable.bg_comment_zhiding_on)
    }

    fun isGoodText(): String = if(isgood==0){
        "加精"
    }else{
        "取消加精"
    }

    fun isGoodTextColor(): Int = if(isgood==0){
        getColor(R.color.base_color)
    }else{
        getColor(R.color.qulusidalse)
    }
}

data class PostHead(
    var jnum: Int=0,
    var _id: Int=0,
    var author: Uinfo= Uinfo(),
    var bid: Int=0,
    var bookname: String="",
    var chapterid: String="",
    var chaptername: String="",
    var content: String="",
    var img: String="",
    var isgood: Int=0,
    var isliked: Int=0,
    var istop: Int=0,
    var likecount: Int=0,
    var reply: ArrayList<Reply> = ArrayList(),
    var replycount: Int=0,
    var type: String="",
    var updated: String=""
)

data class Reply(
    var content: String="",
    var nick: String="",
    var uid: Int=0,
    var img: String=""
)