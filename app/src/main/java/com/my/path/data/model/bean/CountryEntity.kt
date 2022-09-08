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
data class CountryEntity (var st:Int,
                          var country: String="") : Parcelable

