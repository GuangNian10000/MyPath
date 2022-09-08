package com.my.path.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author GuangNian
 * @description:
 * @date : 2021/8/30 5:39 下午
 */
@Suppress("DEPRECATION")
@SuppressLint("ParcelCreator")
@Parcelize
data class FeedBackAdd (
    var st: Int = 0,
    var msg: String? = null,
    var result: String? = null
    ): Parcelable