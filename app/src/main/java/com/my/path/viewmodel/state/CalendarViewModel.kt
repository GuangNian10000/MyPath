package com.my.path.viewmodel.state


import androidx.databinding.ObservableField
import com.my.path.data.model.bean.Book
import com.my.path.data.model.bean.MeGetBook
import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import me.guangnian.mvvm.callback.databind.StringObservableField

class CalendarViewModel : BaseViewModel() {

    var title = "码字日历"

    var book: ObservableField<Book> = ObservableField()

    var meGetBook: MeGetBook?=null
    //当前年
    var theYear  = 0
    //当前月
    var theMonth  = 0
    //当前bid
    var theBid = ""
    //年月
    var yearMonthTitle = StringObservableField()
    //打卡天数
    var dayNum = StringObservableField()
    //打卡字数
    var sizeNum = StringObservableField()
}