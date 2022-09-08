package com.my.path.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import kotlinx.android.parcel.Parcelize

/**
 * @author GuangNian
 * @description:
 * @date : 2022/3/1 2:15 下午
 */


data class AuthorBook(
    var novel: ArrayList<Novel> = ArrayList(),
    var st: Int =0,
    var msg:String=""
)

@Suppress("DEPRECATION")
@SuppressLint("ParcelCreator")
@Parcelize
data class Novel(
    var bid: Int = 0,
    var bsize: Int = 0,
    var chapters: Int = 0,
    var cover: String="",
    var fg: Int= 0,
    var intro: String="",
    var last: String="",
    var lx: Int= 0,
    var name: String="",
    var remark: String="",
    var sd: Int= 0,
    var sdfg: String="",
    var sj: Int= 0,
    var tag: String="",
    var tagid: String= "",
    var type: Int= 0,
    var xx: Int= 0,
    var xxsj: String="",
    var yc: Int= 0,
    var yclx: String="",
    var select:Boolean = false,
    var ztid : Int=0,
    var zt: String=""
): Parcelable, BaseObservable(){


    @get:Bindable
    @set:Bindable
    var obCover: String = cover
        set(value) {
            field = value
            notifyChange()
        }


    @get:Bindable
    @set:Bindable
    var obZtId: Int = ztid
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    @set:Bindable
    var obZt: String = zt
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    @set:Bindable
    var obName: String = name
        set(value) {
            field = value
            notifyChange()
        }


    @get:Bindable
    @set:Bindable
    var obRemark: String = remark
        set(value) {
            field = value
            notifyChange()
        }


    @get:Bindable
    @set:Bindable
    var obIntro: String = intro
        set(value) {
            field = value
            notifyChange()
        }


    @get:Bindable
    @set:Bindable
    var obXXsj: String = xxsj
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    @set:Bindable
    var obSdfg: String = sdfg
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    @set:Bindable
    var obYclx: String = yclx
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    @set:Bindable
    var obTag: String = tag
        set(value) {
            field = value
            notifyChange()
        }

}