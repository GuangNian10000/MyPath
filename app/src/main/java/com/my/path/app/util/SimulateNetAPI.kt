package com.my.path.app.util

import android.content.Context
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.util.*

/**
 * @author GuangNian
 * @description:
 * @date : 2021/6/2 10:04 下午
 */
object SimulateNetAPI {
    /**
     * 获取去最原始的数据信息
     * @return json data
     */
    fun getOriginalFundData(context: Context, fileName: String?): String? {
        var input: InputStream? = null
        try {
            //taipingyang.json文件名称
            input = context.assets.open(fileName!!)
            return convertStreamToString(input)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * input 流转换为字符串
     *
     * @param is
     * @return
     */
    private fun convertStreamToString(`is`: InputStream): String? {
        var s: String? = null
        try {
            //格式转换
            val scanner = Scanner(`is`, "UTF-8").useDelimiter("\\A")
            if (scanner.hasNext()) {
                s = scanner.next()
            }
            `is`.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return s
    }
}