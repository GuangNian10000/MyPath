package com.my.path.app.util

import android.content.Context
import com.my.path.app.ext.getLanguage
import com.my.path.data.model.bean.AreaCode

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.collections.ArrayList

/**
 * @author GuangNian
 * @description:
 * @date : 2022/1/24 4:44 下午
 */
class AreaCodeUtil(val activity:Context?) {
    fun getAreaCode():MutableList<AreaCode>{
        val list: MutableList<AreaCode> =ArrayList()
        activity?.let {
            if(it.getLanguage()){
                list.addAll(
                    Gson().fromJson(
                        SimulateNetAPI.getOriginalFundData(activity, "re_region_cn.json")
                        , object : TypeToken<List<AreaCode?>?>() {}.type))
            }else{
                list.addAll(
                    Gson().fromJson(
                        SimulateNetAPI.getOriginalFundData(activity, "re_region_cn.json")
                        , object : TypeToken<List<AreaCode?>?>() {}.type))
            }
        }
        return list
    }

    fun getAreaCodeCity(country:String):String{
        val list = getAreaCode()
        list.forEach {
            if(country==it.number){
                return it.name.toString()
            }
        }
        return ""
    }
}