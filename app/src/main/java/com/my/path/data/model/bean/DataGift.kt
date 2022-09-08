package com.my.path.data.model.bean

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.my.path.app.ext.strToIntNULL

/**
 * @author GuangNian
 * @description:
 * @date : 2022/3/10 11:15 上午
 */
data class DataGift(
    val count: String="",
    val st: Int=0,
    val sum: String=""
): BaseObservable(){
    @get:Bindable
    @set:Bindable
    var obSum: String = strToIntNULL(  sum.toString())
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    @set:Bindable
    var obCount: String = strToIntNULL( count.toString())
        set(value) {
            field = value
            notifyChange()
        }

}