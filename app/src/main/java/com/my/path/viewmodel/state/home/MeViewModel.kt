package com.my.path.viewmodel.state.home


import androidx.databinding.ObservableField
import com.my.path.data.model.bean.UserData
import me.guangnian.mvvm.base.viewmodel.BaseViewModel

class MeViewModel : BaseViewModel() {
    var userData: ObservableField<UserData> = ObservableField()
}