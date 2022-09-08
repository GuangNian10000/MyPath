package com.my.path.data.model.bean

import com.my.path.app.ext.getHtmlColor

/**
 * @author GuangNian
 * @description:
 * @date : 2022/2/25 11:39 上午
 */
data class TipGift(
    val `data`: ArrayList<TipGiftData> = ArrayList(),
    val st: Int=0
)

data class TipGiftData(
    var bookName:String = "",
    val addtime: String="",
    val money: Int=0,
    val name: String="",
    val num: Int=0,
    val pic: String="",
    val uinfo: Uinfo = Uinfo()
){
    //给《王妃软玉娇香》打赏了1个小心心
    fun getText() = "给《"+bookName+"》"+"打赏了"+getHtmlColor(num.toString() +"个"+name,"#FF544C")

}

data class Uinfo(
    val _id:Int=0,
    val avatar: String="",
    val nick: String="",
    val uid: Int=0,
    val headdress:String="",
    val blv: Int=0,
    val lv: String="",
    val qd: Int=0,
    val type: Int=0,
    val vip: Int=0
)