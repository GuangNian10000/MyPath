package com.my.path.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author GuangNian
 * @description:
 * @date : 2022/2/23 10:26 上午
 */
@Suppress("DEPRECATION")
@SuppressLint("ParcelCreator")
@Parcelize
data class CalendarCode (var st :Int=0, var data: ArrayList<DataDTO> = ArrayList(),var year:Int=0,var month:Int=0): Parcelable

@Suppress("DEPRECATION")
@SuppressLint("ParcelCreator")
@Parcelize
data class DataDTO  (
    var words :Int=0,
    var chapters :Int=0,
    var last :Int=0,
    var date: String = "",
    var highlight:Int=0,
    var isDay:Boolean =false
): Parcelable