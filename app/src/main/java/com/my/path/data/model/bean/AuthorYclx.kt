package com.my.path.data.model.bean

/**
 * @author GuangNian
 * @description:
 * @date : 2022/3/2 3:40 下午
 */

data class AuthorYclx(
    val st: Int=0,
    val yc: ArrayList<Yc> = ArrayList()
)


data class Yc(
    val `data`: ArrayList<YcData>  = ArrayList(),
    val id: Int=0,
    val name: String="",
    var selected :Boolean = false
)


data class YcData(
    val id: Int=0,
    val name: String="",
    var selected :Boolean = false
)