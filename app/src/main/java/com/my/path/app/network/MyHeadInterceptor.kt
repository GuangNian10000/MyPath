package com.my.path.app.network

import com.my.path.app.util.CacheUtil
import me.guangnian.mvvm.util.LogUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 自定义头部参数拦截器，传入heads
 */
class MyHeadInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        CacheUtil.getLoginInfo()?.let {
            builder.addHeader("token",it.token).build()
            LogUtils.debugInfo("uid="+it.uid.toString())
        }
        builder.addHeader("device", "Android").build()
        builder.addHeader("isLogin", CacheUtil.isLogin().toString()).build()

        return chain.proceed(builder.build())
    }

}