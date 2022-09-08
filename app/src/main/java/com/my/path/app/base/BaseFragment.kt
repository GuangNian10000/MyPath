package com.my.path.app.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.my.path.R
import com.my.path.app.ext.dismissLoadingExt
import com.my.path.app.ext.hideSoftKeyboard
import com.my.path.app.ext.showLoadingExt
import com.gyf.immersionbar.ktx.immersionBar
import me.guangnian.mvvm.base.fragment.BaseVmDbFragment
import me.guangnian.mvvm.base.viewmodel.BaseViewModel
import java.util.*

/**
 * 作者　: GuangNian
 * 时间　: 2019/12/21
 * 描述　: 你项目中的Fragment基类，在这里实现显示弹窗，吐司，还有自己的需求操作 ，如果不想用Databind，请继承
 * BaseVmFragment例如
 * abstract class BaseFragment<VM : BaseViewModel> : BaseVmFragment<VM>() {
 */
abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbFragment<VM, DB>(){

    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 懒加载 只有当前fragment视图显示时才会触发该方法
     */
    override fun lazyLoadData() {}

    /**
     * 创建LiveData观察者 Fragment执行onViewCreated后触发
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

    override fun onPause() {
        super.onPause()
        hideSoftKeyboard(activity)
    }

    override fun onStop() {
        super.onStop()
    }



    override fun onResume() {
        super.onResume()
        hideSoftKeyboard(requireActivity())
        if(this.javaClass.name.contains("WritingFragment")||
            this.javaClass.name.contains("MessageFragment")||
            this.javaClass.name.contains("DataFragment")){
            immersionBar {
                statusBarColor(R.color.page_writing)
                statusBarDarkFont(true)
                fitsSystemWindows(true)
            }
        }else if(this.javaClass.name.contains("DataSituationFragment")||
            this.javaClass.name.contains("DataEarningsFragment")||
            this.javaClass.name.contains("MeFragment")||
            this.javaClass.name.contains("DataPaymentFragment")){
            immersionBar {
                statusBarDarkFont(true)
            }
        }else{
            immersionBar {
                statusBarColor(R.color.white)
                statusBarDarkFont(true)
                fitsSystemWindows(true)
            }
        }
    }

    /**
     * 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿  bug
     * 这里传入你想要延迟的时间，延迟时间可以设置比转场动画时间长一点 单位： 毫秒
     * 不传默认 300毫秒
     * @return Long
     */
    override fun lazyLoadTime(): Long {
        return 300
    }

    var lastActionTime : Long = 0

    fun isFast() : Boolean{
        var isRun = false
        val curTime = System.currentTimeMillis()
        if ((curTime - lastActionTime) >= 1000){
            isRun = true
        }
        lastActionTime = curTime
        return isRun
    }

    /**
     * 取map的第x个值
     * */
    fun getIndexMap(map :Map<String,String>):String{
        map.forEach { data->
            return data.key
        }
        return ""
    }

    /**
     * 把毫秒转换成：20:30这里形式
     *
     * @param timeMs
     * @return
     */
    fun stringForTime2(timeMs: Long): String? {
        // 转换成字符串的时间
        val mFormatBuilder = StringBuffer()
        val mFormatter = Formatter(mFormatBuilder, Locale.getDefault())
        val totalSeconds = timeMs / 1000
        val seconds = totalSeconds % 60
        var minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        if (hours > 0) {
            minutes += hours * 60
        }
        mFormatBuilder.setLength(0)
        return mFormatter.format("%02d:%02d", minutes, seconds).toString()
    }

    fun  <T> createModel(viewModel:Class<T>): T? {
        return ViewModelProvider(this)[viewModel as Class<BaseViewModel>] as T
    }
}