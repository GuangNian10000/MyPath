package com.my.path.data.model.bean

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

/**
 * @author GuangNian
 * @description:
 * @date : 2022/2/25 10:42 上午
 */

data class TipIndex(
    val comment: MassageIndex=MassageIndex(),
    val gift: MassageIndex=MassageIndex(),
    val st: Int=0,
    val ticket: MassageIndex=MassageIndex(),
    val hongbao: MassageIndex=MassageIndex()
):BaseObservable()

data class MassageIndex(
    val mess: String="",
    val new: Int=0
):BaseObservable(){
    @get:Bindable
    @set:Bindable
    var num: String = new.toString()
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    @set:Bindable
    var desc: String = mess
        set(value) {
            field = value
            notifyChange()
        }
}

//data class Gift(
//    val mess: String="",
//    val new: Int=0
//):BaseObservable()
//
//data class Ticket(
//    val mess: String="",
//    val new: Int=0
//):BaseObservable()