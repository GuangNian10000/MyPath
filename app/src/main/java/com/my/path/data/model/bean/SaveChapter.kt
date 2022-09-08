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
data class SaveChapter (var st:Int=0,
                        var msg: String="",
                        var result: String="", val cid:Int=0, val content:String="", val ispublic:String="") : Parcelable

