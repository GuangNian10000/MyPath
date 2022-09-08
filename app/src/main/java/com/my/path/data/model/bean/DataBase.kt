package com.my.path.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.my.path.app.ext.strToIntNULL
import kotlinx.android.parcel.Parcelize

/**
 * @author GuangNian
 * @description:
 * @date : 2022/8/17 1:48 下午
 */
@Suppress("DEPRECATION")
@SuppressLint("ParcelCreator")
@Parcelize
data class DataBase(
    val `data`: ArrayList<BaseData> = ArrayList(),
    val st: Int=0
): Parcelable

@Suppress("DEPRECATION")
@SuppressLint("ParcelCreator")
@Parcelize
data class BaseData(
    val bid: Int=0,
    val bookshelf: Int=0,
    val cover: String="",
    val gift: Int=0,
    val hits: Int=0,
    val lastin: String="",
    val name: String="",
    val subscribe: Int=0,
    val ticket: String="",
    val totalin: String="",
    var select :Boolean = false
): Parcelable, BaseObservable(){

    @get:Bindable
    @set:Bindable
    var obHits: String = strToIntNULL(hits.toString())
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    @set:Bindable
    var obCover: String = cover
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
    var obTicket: String = strToIntNULL( ticket) //今日推荐票
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    @set:Bindable
    var obGits: String = strToIntNULL( hits.toString()) //昨日浏览量
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    @set:Bindable
    var obBookshelf: String = strToIntNULL( bookshelf.toString() )//今日收藏量
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    @set:Bindable
    var obSubscribe: String = strToIntNULL( subscribe.toString()) //本月订阅
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    @set:Bindable
    var obGift: String = strToIntNULL( gift.toString()) //本月礼物
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    @set:Bindable
    var obLastin: String = strToIntNULL( lastin) //上月稿费
        set(value) {
            field = value
            notifyChange()
        }
    @get:Bindable
    @set:Bindable
    var obTotalin: String = strToIntNULL( totalin )//累计稿费
        set(value) {
            field = value
            notifyChange()
        }
}