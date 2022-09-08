package com.my.path.app.util

import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {
    /**
     * 时间戳转日期
     * */
    fun timestampToDate(time:Long):String{
        return SimpleDateFormat("yyyy-MM-dd HH:mm").format(time*1000)
    }

    fun timestampToTime(time:Long): Date {
        return getStringToData(SimpleDateFormat("yyyy-MM-dd HH:mm").format(time*1000))
    }
    /**
     * 字符串转时间
     * */
    fun getStringToData(str:String): Date {
        var date = Date()
        try {
            date = SimpleDateFormat("yyyy-MM-dd HH:mm").parse(str)
        }catch (e:java.lang.Exception){}
        return date
    }

    /**
     * 字符串转时间戳
     * */
    fun stringToTimestamp(str:String):Long{
        return SimpleDateFormat("yyyy-MM-dd HH:mm").parse(str, ParsePosition(0)).time/1000
    }


    /**
     * 时间转字符串
     * */
    fun getDataToStringYMD(date:Date):String{
        //可根据需要自行截取数据显示
        return SimpleDateFormat("yyyy-MM-dd HH:mm").format(date)
    }
}