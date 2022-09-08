package com.my.path.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author GuangNian
 * @description:
 * @date : 2022/2/23 10:03 上午
 */
@Suppress("DEPRECATION")
@SuppressLint("ParcelCreator")
@Parcelize
data class MeUpdate(var day:Int, var codeNum:Long, var isNull:Boolean,val highlight:Int,var isDay:Boolean = false): Parcelable