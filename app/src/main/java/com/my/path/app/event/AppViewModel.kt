package com.my.path.app.event

import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.callback.livedata.event.EventLiveData

/**
 * 作者　: GuangNian
 * 时间　: 2019/12/23
 * 描述　:APP全局的ViewModel，可以存放公共数据，当他数据改变时，所有监听他的地方都会收到回调,也可以做发送消息
 * 比如 全局可使用的 地理位置信息，账户信息,App的基本配置等等，
 */
class AppViewModel : BaseViewModel() {

    //App的账户信息
    //var userInfo = UnPeekLiveData.Builder<UserInfo>().setAllowNullValue(true).create()

    init {

    }
    //国别码
    var userCountryCode = EventLiveData<String>()
    var userCountryName = EventLiveData<String>()
}