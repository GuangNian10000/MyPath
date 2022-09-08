package com.my.path.data.model.bean

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author GuangNian
 * @description:
 * @date : 2022/3/10 11:12 上午
 */
data class DataSurvey(
    val bookshelf: ArrayList<dataSur> = ArrayList(),
    val hits: ArrayList<dataSur> = ArrayList(),
    val rank: ArrayList<dataSur> = ArrayList(),
    val st: Int=0,
    val ticket: ArrayList<dataSur> = ArrayList()
)

data class dataSur(
    val date: String="",
    val num: Int=0
){
    fun getDateG()=getDataToString(getStringToData(date))
}

data class Bookshelf(
    val date: String="",
    val num: String=""
){
    fun getDateG()=getDataToString(getStringToData(date))
}

data class Hit(
    val date: String="",
    val num: String=""
){
    fun getDateG():String= getDataToString(getStringToData(date))

}

data class Rank(
    val date: String="",
    val num: String=""
){
    fun getDateG()=getDataToString(getStringToData(date))
}

data class surveyTicket(
    val date: String="",
    val num: String=""
){
    fun getDateG()=getDataToString(getStringToData(date))
}

/**
 * 字符串转时间
 * */
fun getStringToData(str:String): Date {
    var date = Date()
    try {
        date = SimpleDateFormat("yyyy-M-dd").parse(str)
    }catch (e:java.lang.Exception){}
    return date
}

/**
 * 时间转字符串
 * */
fun getDataToString(date:Date):String{
    //可根据需要自行截取数据显示
    return SimpleDateFormat("yyyy-M-dd").format(date)
}