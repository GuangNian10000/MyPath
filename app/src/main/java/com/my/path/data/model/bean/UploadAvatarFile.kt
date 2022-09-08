package com.my.path.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author GuangNian
 * @description:
 * @date : 2021/5/27 6:10 下午
 */
@Suppress("DEPRECATION")
@SuppressLint("ParcelCreator")
@Parcelize
data class UploadAvatarFile (
    var st: Int = 0,
    var msg: String="",
    var result: String="",
    var url: String="",
    var pic: String="",
    var msgId: String=""
    ): Parcelable