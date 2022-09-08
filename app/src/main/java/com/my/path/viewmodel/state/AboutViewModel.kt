package com.my.path.viewmodel.state

import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.callback.databind.StringObservableField


/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　:关于我们的ViewModel
 */
class AboutViewModel : BaseViewModel() {
    //版本号
    var version =  StringObservableField()
}