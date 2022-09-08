package com.my.path.viewmodel.state


import com.my.path.data.model.bean.Picture
import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.callback.databind.StringObservableField
import java.util.ArrayList


/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　:意见反馈的ViewModel
 */
class FeedBackViewModel : BaseViewModel() {

    //问题描述
    var etDescribe  = StringObservableField()
    //最大长度
    var maxTextLength = StringObservableField("/200")

    //当前长度
    var theTextLength  = StringObservableField("0")

    //最大长度
    var maxPhoneLength = StringObservableField("/3")
    //当前长度
    var thePhoneLength = StringObservableField()

    //图片
    var listPicture :MutableList<Picture> = ArrayList()

    val simpleTextWatcher = object : LoginRegisterViewModel.SimpleTextWatcher(){
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            val length = s.length
            if(length>=200){
                etDescribe.set(s.substring(0,200))
            }
            theTextLength.set(s.length.toString())
        }
    }
}