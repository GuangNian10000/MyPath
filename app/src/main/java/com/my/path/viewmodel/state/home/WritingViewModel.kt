package com.my.path.viewmodel.state.home


import com.my.path.data.model.bean.Novel
import me.guangnian.mvvm.base.viewmodel.BaseViewModel

class WritingViewModel : BaseViewModel() {
    var title = "写作"
    var mBookList:ArrayList<Novel> = ArrayList()
}