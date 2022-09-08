package com.my.path.app.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.preference.PreferenceManager
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.my.path.R
import com.my.path.data.model.bean.ChapterContent
import com.my.path.weight.loadCallBack.LoadingCallback
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kingja.loadsir.core.LoadService
import me.guangnian.mvvm.base.appContext
import me.guangnian.mvvm.util.LogUtils


object SettingUtil {
    /**
     * 是否跟随系统语言
     * 用于判断语言是跟随系统还是跟随用户设置
     * */
    fun isSystemLanguages(context: Context):Boolean{
        val setting = PreferenceManager.getDefaultSharedPreferences(context)
        return  setting.getBoolean("setLang", true)
    }

    /**
     * 是否设置过语言
     * 用于判断语言是跟随系统还是跟随用户设置
     * 是否跟随系统，而不是跟随app设置
     * */
    fun setIsSystemLanguages(context: Context,isSystem:Boolean){
        val setting = PreferenceManager.getDefaultSharedPreferences(context)
        setting.edit().putBoolean("setLang", isSystem).apply()
    }


    fun getOneColorStateList(context: Context): ColorStateList {
        val colors = intArrayOf(getColor(context))
        val states = arrayOfNulls<IntArray>(1)
        states[0] = intArrayOf()
        return ColorStateList(states, colors)
    }

    fun getOneColorStateList(color: Int): ColorStateList {
        val colors = intArrayOf(color)
        val states = arrayOfNulls<IntArray>(1)
        states[0] = intArrayOf()
        return ColorStateList(states, colors)
    }

    /**
     * 获取当前主题颜色
     */
    fun getColor(context: Context): Int {
        val setting = PreferenceManager.getDefaultSharedPreferences(context)
        val defaultColor = ContextCompat.getColor(context, R.color.base_color)
        val color = setting.getInt("color", defaultColor)
        return if (color != 0 && Color.alpha(color) != 255) {
            defaultColor
        } else {
            color
        }
    }

    fun getColorStateList(context: Context): ColorStateList {
        val colors = intArrayOf(getColor(context), ContextCompat.getColor(context, R.color.colorGray))
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(android.R.attr.state_checked, android.R.attr.state_checked)
        states[1] = intArrayOf()
        return ColorStateList(states, colors)
    }

    /**
     * 设置loading的颜色 加载布局
     */
    fun setLoadingColor(color:Int,loadsir: LoadService<Any>) {
        loadsir.setCallBack(LoadingCallback::class.java) { _, view ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.findViewById<ProgressBar>(R.id.loading_progress).indeterminateTintMode = PorterDuff.Mode.SRC_ATOP
                view.findViewById<ProgressBar>(R.id.loading_progress).indeterminateTintList = getOneColorStateList(color)
            }
        }
    }

    fun setWriteTemporaryNUll(context: Context){
        val setting = PreferenceManager.getDefaultSharedPreferences(context)
        setting.edit().putString("chapterContent", "").apply()
    }

    /**
     * 设置写作临时缓存内容
     * */
    fun setWriteTemporaryCache(data: ChapterContent){
        val setting = PreferenceManager.getDefaultSharedPreferences(appContext)
        setting.edit().putString("chapterContent", Gson().toJson(data)).apply()
        LogUtils.debugInfo(Gson().toJson(data))
    }

    /**
     * 获取写作临时缓存内容
     * */
    fun getWriteTemporaryCache(context: Context):ChapterContent?{
        val setting = PreferenceManager.getDefaultSharedPreferences(context)
        return Gson().fromJson(setting.getString("chapterContent", "")
            , object : TypeToken<ChapterContent>() {}.type)
    }
}
