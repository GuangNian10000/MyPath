package com.my.path.app


import androidx.multidex.MultiDex
import com.my.path.BuildConfig
import com.my.path.app.event.AppViewModel

import com.hjq.toast.ToastUtils
import com.huantansheng.easyphotos.EasyPhotos
import com.mallotec.reb.localeplugin.LocalePlugin
import com.my.path.app.event.EventViewModel
import com.my.path.app.ext.getLanguage
import com.my.path.app.ext.setLanguage
import com.my.path.app.network.ApiService.Companion.SERVER_URL_CN
import com.my.path.app.util.CacheUtil
import com.my.path.app.util.moshi.MoshiConvert
import com.my.path.app.weight.loadCallBack.*
import com.my.path.weight.loadCallBack.LoadingCallback
import com.drake.brv.BR
import com.drake.brv.utils.BRV
import com.drake.net.NetConfig
import com.drake.net.interceptor.RequestInterceptor
import com.drake.net.okhttp.setConverter
import com.drake.net.okhttp.setDebug
import com.drake.net.okhttp.setRequestInterceptor
import com.drake.net.request.BaseRequest
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.mmkv.MMKV
import me.guangnian.mvvm.base.BaseApp
import me.jessyan.autosize.AutoSizeConfig
import java.util.concurrent.TimeUnit


/**
 * 作者　: GuangNian
 * 时间　: 2019/12/23
 * 描述　:
 */

//Application全局的ViewModel，里面存放了一些账户信息，基本配置信息等
val appViewModel: AppViewModel by lazy { App.appViewModelInstance }

//Application全局的ViewModel，用于发送全局通知操作
val eventViewModel: EventViewModel by lazy { App.eventViewModelInstance }

class App : BaseApp() {

    companion object {
        lateinit var instance: App
        lateinit var eventViewModelInstance: EventViewModel
        lateinit var appViewModelInstance: AppViewModel
    }

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this.filesDir.absolutePath + "/mmkv")
        instance = this
        eventViewModelInstance = getAppViewModelProvider().get(EventViewModel::class.java)
        appViewModelInstance = getAppViewModelProvider().get(AppViewModel::class.java)
        MultiDex.install(this)
        //初始化语言
        LocalePlugin.init(this)
        initLanguages()
        //预加载图片选择器
        EasyPhotos.preLoad(this)
        //toast
        ToastUtils.init(this)
        //如果不想让 App 内的字体大小跟随系统设置中字体大小的改变，请调用
        AutoSizeConfig.getInstance().isExcludeFontScale = true
        //界面加载管理 初始化
        initLoadSir()
        //初始化网络请求
        initNet()
        // 初始化BindingAdapter的默认绑定ID, 如果不使用DataBinding并不需要初始化
        initBRV()
    }

    private fun initBRV(){
        BRV.modelId = BR.m
        //刷新布局要求必须先初始化, 推荐在Application中
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout -> MaterialHeader(this) }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout -> ClassicsFooter(this) }
    }

    private fun initLoadSir(){
        LoadSir.beginBuilder()
            .addCallback(LoadingCallback())//加载
            .addCallback(ErrorCallback())//错误
            .addCallback(EmptyCallback())//空
            .addCallback(EmptyDataCallback())//空
            .addCallback(EmptyChapterCallback())//空
            .addCallback(EmptyWritingCallback())//空
            .addCallback(ErrorPenNameCallback())//空
            .addCallback(EmptyRiliallback())//空
            .addCallback(EmptyMessageback())
            .setDefaultCallback(SuccessCallback::class.java)//设置默认加载状态页
            .commit()
    }

    private fun initNet(){
        NetConfig.initialize(SERVER_URL_CN, this) {
            // 超时配置, 默认是10秒, 设置太长时间会导致用户等待过久
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            setConverter(MoshiConvert())
            setDebug(BuildConfig.DEBUG)
            setRequestInterceptor(object : RequestInterceptor {
                override fun interceptor(request: BaseRequest) {
                    CacheUtil.getLoginInfo()?.let {
                        request.param("client", "Net")
                        request.setHeader("token", it.token)
                    }
                }
            })
        }
    }

    /**
     * 初始化语言
     * */
    fun initLanguages(){
        setLanguage(getLanguage())
    }
}
