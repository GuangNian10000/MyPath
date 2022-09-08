package com.my.path.viewmodel.state


import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.callback.databind.BooleanObservableField
import me.guangnian.mvvm.callback.databind.StringObservableField

class AccountSecurityViewModel : BaseViewModel() {
    //手机绑定
    var phoneBindDes = StringObservableField()
    var phoneBindButDes = StringObservableField()
    var phoneBindState = BooleanObservableField()
    //邮箱绑定
    var emailBindDes = StringObservableField()
    var emailBindButDes = StringObservableField()
    var emailBindState = BooleanObservableField()
    //密码设置
    var pwdBindDes = StringObservableField()
    var pwdBindButDes = StringObservableField()
    var pwdBindState = BooleanObservableField()
}