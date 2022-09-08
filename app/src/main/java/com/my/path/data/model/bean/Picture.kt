package com.my.path.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import android.widget.ProgressBar
import com.huantansheng.easyphotos.models.album.entity.Photo
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.IgnoredOnParcel

/**
 * @author GuangNian
 * @description:
 * @date : 2021/7/20 4:41 下午
 */
@Suppress("DEPRECATION")
@SuppressLint("ParcelCreator")
@Parcelize
data class Picture(var isChoose: Boolean, var photo: Photo?=null) : Parcelable {
    @IgnoredOnParcel
    var serviceUrl: String? = ""
    @IgnoredOnParcel
    var view: ProgressBar? = null
}