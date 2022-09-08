package com.my.path.data.model.bean

/**
 * @author GuangNian
 * @description:
 * @date : 2022/3/16 2:26 下午
 */
data class Payment(
    val `data`: ArrayList<DataPayment> = ArrayList(),
    val st: Int=0
)

data class DataPayment(
    val date: String ="",
    val money: String =""
)