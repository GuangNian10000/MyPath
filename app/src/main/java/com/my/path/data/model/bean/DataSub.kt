package com.my.path.data.model.bean

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.my.path.app.ext.strToIntNULL

/**
 * @author GuangNian
 * @description:
 * @date : 2022/3/15 11:01 上午
 */
data class DataSub(
    val count: Int=0,
    val lastweek: Int=0,
    val st: Int=0,
    val today: Int=0,
    val yesterday: Int=0,
    val month:Int=0,
    val total:Int=0
): BaseObservable(){
    @get:Bindable
    @set:Bindable
    var obLastweek: String = strToIntNULL( lastweek.toString())
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    @set:Bindable
    var obToday: String = strToIntNULL( today.toString())
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    @set:Bindable
    var obYesterday: String = strToIntNULL( yesterday.toString())
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    @set:Bindable
    var obMonth: String = month.toString()
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    @set:Bindable
    var obTotal: String = strToIntNULL( total.toString())
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    @set:Bindable
    var obCount: String =strToIntNULL(  count.toString())
        set(value) {
            field = value
            notifyChange()
        }


}