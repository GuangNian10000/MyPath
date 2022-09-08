package com.my.path.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author GuangNian
 * @description:
 * @date : 2022/2/21 10:33 上午
 */
@Suppress("DEPRECATION")
@SuppressLint("ParcelCreator")
@Parcelize
data class WebResponse(var title :String,
                       var url:String) : Parcelable