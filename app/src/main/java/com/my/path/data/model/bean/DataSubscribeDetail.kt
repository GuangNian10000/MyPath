package com.my.path.data.model.bean

/**
 * @author GuangNian
 * @description:
 * @date : 2022/3/10 11:14 上午
 */
data class DataSubscribeDetail(
    val `data`: ArrayList<DataSubscribe> = ArrayList(),
    val st: Int=0
)

data class DataSubscribe(
    val amount: String="",
    val count: String="",
    val name: String=""
)