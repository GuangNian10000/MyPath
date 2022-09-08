package com.my.path.data.model.bean
import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 作者　: GuangNian
 * 时间　: 2019/12/23
 * 描述　: 账户信息
 */
@Suppress("DEPRECATION")
@SuppressLint("ParcelCreator")
@Parcelize

data class LoginInfo(val st :Int=0,
                     val msg:String="",
                     val result:String="",
                     var phone:String="",
                     var uid: Int=0,
                     val token: String="") : Parcelable
