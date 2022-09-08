package com.my.path.viewmodel.state.writing


import androidx.databinding.ObservableField
import com.my.path.data.model.bean.Novel
import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.callback.databind.StringObservableField

class WritingChangeViewModel : BaseViewModel() {
    var title=StringObservableField("修改作品")
    var novel: ObservableField<Novel> = ObservableField()
}