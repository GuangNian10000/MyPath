package com.my.path.viewmodel.state.message


import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.callback.databind.StringObservableField

class CommentDetailsViewModel : BaseViewModel() {
    var id=0
    var bid =0
    var currentTitle = StringObservableField("")

    //评论
    var type = 0 //排序
    fun getCommentType() = typeMap[type]
    val typeMap: MutableList<String> = mutableListOf(
        "default","new","hot")
    val typeTextMap: MutableList<String> = mutableListOf(
        "推荐","最新","最热")
    var textType = StringObservableField(typeTextMap[type])

    var textTypeMap = typeTextMap[type]

    var Jnum = 0
}