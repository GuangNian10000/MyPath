package com.my.path.viewmodel.state.data

import androidx.databinding.ObservableField
import com.my.path.app.util.CacheUtil
import com.my.path.data.model.bean.*
import me.guangnian.mvvm.base.viewmodel.BaseViewModel

class DataSituationViewModel : BaseViewModel() {
    var baseData: ObservableField<BaseData> = ObservableField()
    var dataSub : ObservableField<DataSub> = ObservableField()
    var dataGift : ObservableField<DataGift> = ObservableField()

    var dataSurvey:DataSurvey=DataSurvey()

    var payment:Payment= Payment()

    fun avatar() =
        if(null!=CacheUtil.getUserData()){
            CacheUtil.getUserData()?.avatar
        }else{
            ""
        }
}