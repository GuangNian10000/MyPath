package com.my.path.viewmodel.state.chapter


import com.my.path.data.model.bean.Novel
import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.callback.databind.BooleanObservableField
import me.guangnian.mvvm.callback.databind.IntObservableField
import me.guangnian.mvvm.callback.databind.StringObservableField

class ChapterListViewModel : BaseViewModel() {

    var title=StringObservableField()

    val isReverseOrder= BooleanObservableField(false)

    var novel = Novel()

    val allChapterNum = IntObservableField(0)
    val draftChapterNum = IntObservableField(0)
}