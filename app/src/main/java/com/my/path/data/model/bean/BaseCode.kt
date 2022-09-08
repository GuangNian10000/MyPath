package com.my.path.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author GuangNian
 * @description:消息验证码
 * @date : 4/15/21 3:28 PM
 */
@Suppress("DEPRECATION")
@SuppressLint("ParcelCreator")
@Parcelize
data class BaseCode (var st:Int=0,
                     var msg: String="",
                     var result: String="",var position:Int=0,var additional:String="") : Parcelable

