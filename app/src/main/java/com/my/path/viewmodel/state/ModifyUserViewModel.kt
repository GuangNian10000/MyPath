package com.my.path.viewmodel.state

import com.my.path.data.model.bean.UserData
import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.callback.databind.StringObservableField


/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　:修改资料的ViewModel
 */
class ModifyUserViewModel : BaseViewModel() {
    //笔名
    var penName =  StringObservableField()
    //昵称
    var userName =  StringObservableField()
    //性别
    var sex =  StringObservableField()
    //账号
    var userId =  StringObservableField()
    //用户信息
    var meGetInFo : UserData?=null
}