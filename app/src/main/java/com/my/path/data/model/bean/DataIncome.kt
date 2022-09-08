package com.my.path.data.model.bean

/**
 * @author GuangNian
 * @description:
 * @date : 2022/3/16 2:25 下午
 */
data class DataIncome(
    val `data`: ArrayList<Income> = ArrayList(),
    val st: Int=0
)

data class Income(
    val date: String="",
    val money: String="",
    val pay: String="",
    val remark: String="",
    val type: String=""
)