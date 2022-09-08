package com.my.path.app.util

import MyKotlinJsonAdapterFactory
import com.my.path.app.util.moshi.MoshiArrayListJsonAdapter
import com.my.path.app.util.moshi.MyStandardJsonAdapters
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import me.guangnian.mvvm.ext.util.toJson
import java.lang.reflect.Type

/**
 * @author GuangNian
 * @description:
 * @date : 2022/6/2 2:49 下午
 */
object MoShi {
    fun  <T> fromJson(str:String,type:Class<T>): T? {
        try {
            val moshi: Moshi = Moshi.Builder()
                .add(MyKotlinJsonAdapterFactory())
                .add(MyStandardJsonAdapters.FACTORY)
                .add(MoshiArrayListJsonAdapter.FACTORY)
                .build()
            return moshi.adapter(type).fromJson(str)
        }catch (e:Exception){}
        return null
    }

    fun  <T> fromJsonArr(str:String,cls:Class<T>): ArrayList<T>? {
        val moshi: Moshi = Moshi.Builder()
            .add(MyKotlinJsonAdapterFactory())
            .add(MyStandardJsonAdapters.FACTORY)
            .add(MoshiArrayListJsonAdapter.FACTORY)
            .build()
        val type: Type = Types.newParameterizedType(
            MutableList::class.java,
            cls
        )

        val adapter: JsonAdapter<ArrayList<T>> = moshi.adapter(type)
        return adapter.fromJson(str)
    }

    fun <T> toJson(type:Class<T>){
        type.toJson()
    }
}