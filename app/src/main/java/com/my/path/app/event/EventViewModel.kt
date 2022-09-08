package com.my.path.app.event

import com.my.path.data.model.bean.InApp
import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.callback.livedata.event.EventLiveData


/**
 * 作者　: GuangNian
 * 时间　: 2019/5/2
 * 描述　:APP全局的ViewModel，可以在这里发送全局通知替代EventBus，LiveDataBus等
 */
class EventViewModel : BaseViewModel() {

    //登录状态
    val isLogin = EventLiveData<Boolean>()
    var onInLogin = EventLiveData<Boolean>()


    val writingEvent = EventLiveData<Boolean>() //刷新作品列表
    val inAddWriting = EventLiveData<Boolean>() //去创建作品
    val inPenName = EventLiveData<Boolean>() //笔名设置
    val inAddChapter = EventLiveData<Boolean>() //创建章节

    val allChapterNum = EventLiveData<Int>()  //全部章节数量
    val draftChapterNum = EventLiveData<Int>()  //草稿章节数量

    val draftBoxEvent = EventLiveData<Boolean>() //刷新列表
    val isReverseOrder = EventLiveData<Boolean>() //刷新列表

    val inAppFragment =  EventLiveData<InApp>()

    val onSaveChapter =  EventLiveData<Boolean>() //保存章节内容

}