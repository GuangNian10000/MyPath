package com.my.path.app.util.moshi

import MyKotlinJsonAdapterFactory
import com.drake.net.convert.JSONConvert
import com.squareup.moshi.Moshi
import java.lang.reflect.Type

/**
 * @author GuangNian
 * @description:
 * @date : 2022/8/10 3:34 下午
 */
class MoshiConvert : JSONConvert(code = "code", message = "msg", success = "200") {
    val moshi = Moshi.Builder()
        .add(MyKotlinJsonAdapterFactory())
        .add(MyStandardJsonAdapters.FACTORY)
        .add(MoshiArrayListJsonAdapter.FACTORY)
        .build()

    override fun <S> String.parseBody(succeed: Type): S? {
        return moshi.adapter<S>(succeed).fromJson(this)
    }
}