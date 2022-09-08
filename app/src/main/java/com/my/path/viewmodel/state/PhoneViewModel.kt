package com.my.path.viewmodel.state


import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.callback.databind.StringObservableField

class PhoneViewModel : BaseViewModel() {
    // 地区
    var region =  StringObservableField("中国大陆")
    // ip
    var ipCode = StringObservableField("+86")
    //手机号
    var userName = StringObservableField()
}