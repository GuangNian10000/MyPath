package com.my.path.data.model.bean
import androidx.databinding.BaseObservable


/**
 * @author GuangNian
 * @description:
 * @date : 2022/5/12 10:04 上午
 */

data class UserData(
    val apple: Int = 0,
    val avatar: String ="",
    val bgpic: String ="",
    var city: String ="",
    val daka: Int = 0,
    var day: Int = 0,
    val discount: Int = 0,
    val email: String ="",
    val facebook: Int = 0,
    val fans: String ="",
    val following: String ="",
    val headdress: String ="",
    var isfollow: Int = 0,
    val last: Int = 0,
    val lv: String ="",
    val blv:Int =0,
    val mobile: String ="",
    val money: Int = 0,
    var month: Int = 0,
    var nick: String ="",
    val pass: Int = 0,
    val qq: Int = 0,
    val score: Int = 0,
    var sex: Int = 0,
    var sign: String ="",
    val st: Int = 0,
    val tasks_left: Int = 0,
    val tickets: Int = 0,
    val uid: Int = 0,
    val vip: Int = 0,
    val vipexp: Int =0,
    val wx: Int = 0,
    val author:String="",
    var year: Int = 0
): BaseObservable()