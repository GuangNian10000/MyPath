package com.my.path.viewmodel.state.login


import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.callback.databind.BooleanObservableField

/**
 * 用户服务协议
 * */
class UserServiceAgreementViewModel : BaseViewModel() {
    //是否选中
    var isChecked = BooleanObservableField(false)
}