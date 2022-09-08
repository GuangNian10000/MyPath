package com.my.path.viewmodel.state.home


import androidx.databinding.ObservableField
import com.my.path.data.model.bean.TipIndex
import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.callback.databind.StringObservableField

class MessageViewModel : BaseViewModel() {
    var title = StringObservableField("消息")
    var tipIndex : ObservableField <TipIndex> = ObservableField()

}