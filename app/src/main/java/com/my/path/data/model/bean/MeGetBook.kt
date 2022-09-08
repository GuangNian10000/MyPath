package com.my.path.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.databinding.BaseObservable
import kotlinx.android.parcel.Parcelize

/**
 * @author GuangNian
 * @description:
 * @date : 2022/2/23 10:44 上午
 */
@Suppress("DEPRECATION")
@SuppressLint("ParcelCreator")
@Parcelize
data class MeGetBook(
    val books: ArrayList<Book> = ArrayList(),
    val st: Int=0
): Parcelable

@Suppress("DEPRECATION")
@SuppressLint("ParcelCreator")
@Parcelize
data class Book(
    val _id: Int=0,
    val author: String="",
    val cat: String="",
    val cover: String="",
    val followerCount: Int=0,
    val hcover: String="",
    val isfree: Int=0,
    val isvip: String="",
    val lastchapter: String="",
    val posted: String="",
    val shortIntro: String="",
    val size: String="",
    val tag: String="",
    val timestamp: Int=0,
    val title: String="",
    val updated: String="",
    val zt: String="",
    var isSelect:Boolean = false
): Parcelable, BaseObservable(){

}