package com.my.path.data.model.bean

/**
 * @author GuangNian
 * @description:
 * @date : 2022/8/15 6:57 下午
 */
data class HongBaoData(
    val `data`: ArrayList<HongBao> = ArrayList(),
    val st: Int=0
)

data class HongBao(
    val hid:Int=0,
    val addtime: String="",
    val draw_money: String="",
    val draw_num: Int=0,
    val money: Int=0,
    val num: Int=0,
    val slogan: String="",
    val type: String="",
    val uinfo: Uinfo=Uinfo()
)