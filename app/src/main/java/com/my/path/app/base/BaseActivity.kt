package com.my.path.app.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.my.path.app.ext.dismissLoadingExt
import com.my.path.app.ext.showLoadingExt

import me.guangnian.mvvm.base.activity.BaseVmVbActivity
import me.guangnian.mvvm.base.viewmodel.BaseViewModel


/**
 * 时间　: 2019/12/21
 * 作者　: GuangNian
 * 描述　: 你项目中的Activity基类，在这里实现显示弹窗，吐司，还有加入自己的需求操作 ，如果不想用 Databind，请继承
 * BaseVmActivity例如
 * abstract class BaseActivity<VM : BaseViewModel> : BaseVmActivity<VM>() {
 */
abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : BaseVmVbActivity<VM, VB>() {

    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 创建liveData观察者
     */
    override fun createObserver() {}

    /**
     * 打开等待框
     */
    override fun showLoading(message: String) {
        showLoadingExt(message)
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
        dismissLoadingExt()
    }

//    override fun getResources(): Resources {
//        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
//        return super.getResources()
//    }

//    override fun getResources(): Resources? {
//        val resources: Resources = super.getResources()
//        val configuration = Configuration()
//        configuration.setToDefaults()
//        resources.updateConfiguration(configuration, resources.displayMetrics)
//        return resources
//    }

   /* *//**
     * 在任何情况下本来适配正常的布局突然出现适配失效，适配异常等问题，只要重写 Activity 的 getResources() 方法
     *//*
    override fun getResources(): Resources {
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
        return super.getResources()
    }*/
}