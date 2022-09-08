package com.my.path.app.util

import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.my.path.app.eventViewModel
import com.my.path.app.ext.inInLoginFragment
import com.my.path.data.model.bean.UserData
import com.my.path.data.model.bean.LoginInfo
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import me.guangnian.mvvm.ext.util.toJson

object CacheUtil {
    /**
     * 获取保存的登录信息
     */
    fun getLoginInfo(): LoginInfo? {
        val kv = MMKV.mmkvWithID("app")
        val userStr = kv.decodeString("user")
        return if (TextUtils.isEmpty(userStr)) {
           null
        } else {
            Gson().fromJson(userStr, LoginInfo::class.java)
        }
    }

    /**
     * 设置登录信息
     */
    fun setLoginInfo(userResponse: LoginInfo?) {
        val kv = MMKV.mmkvWithID("app")
        if (userResponse == null) {
            kv.encode("user", "")
            if(isLogin()){
                setIsLogin(false)
            }
        } else {
            kv.encode("user", Gson().toJson(userResponse))
            if(!isLogin()){
                setIsLogin(true)
            }
        }

    }

    /**
     * 设置用户信息
     * */
    fun setUserData(userResponse: UserData?) {
        val kv = MMKV.mmkvWithID("app")
        kv.encode("userData",userResponse.toJson())
    }

    /**
     * 获取用户信息
     * */
    fun getUserData(): UserData?{
        val kv = MMKV.mmkvWithID("app")
        val userStr = kv.decodeString("userData")
        return if(null==userStr||TextUtils.isEmpty(userStr)){
            null
        } else {
            MoShi.fromJson(userStr,UserData::class.java)
        }
    }

    /**
     * 获取token
     * */
    fun getToken():String{
        val userInfo = getLoginInfo()
        return userInfo?.token ?: ""
    }

    /**
     * 设置账户信息
     * 设置手机号
     */
    fun setUserPhone(phone: String) {
        val user = getLoginInfo()
        if (user == null) {
            setIsLogin(false)
        } else {
            user.phone = phone
            setLoginInfo(user)
        }
    }

    //设置用户id
    fun setUserUid(uid: Int) {
        val user = getLoginInfo()
        if (user == null) {
            setIsLogin(false)
        } else {
            user.uid = uid
            setLoginInfo(user)
        }
    }

    //获取用户id
    fun getUid():Int{
        getLoginInfo()?.let {
            return it.uid
        }
        return -101
    }

    /**
     * 是否已经登录
     */
//    fun isLogin(fragment: Fragment?=null): Boolean {
//        val kv = MMKV.mmkvWithID("app")
//        val login = kv.decodeBool("login", false)
//        if(!login){
//            fragment?.apply {
//                inLoginFragment()
//            }
//        }
//        return login
//    }

    /**
     * 是否已经登录
     */
    fun isLogin(fragment: Fragment?=null): Boolean {
        val kv = MMKV.mmkvWithID("app")
        val login = kv.decodeBool("login", false)
        if(!login){
            fragment?.apply {
                if(isAdded){
                   //inLoginFragment()
                      fragment.requireActivity().inInLoginFragment()
                }
            }
        }
        return login
    }


    /**
     * 设置是否已经登录
     */
    fun setIsLogin(isLogin: Boolean) {
        val kv = MMKV.mmkvWithID("app")
        kv.encode("login", isLogin)
        eventViewModel.isLogin.value = isLogin
    }

    /**
     * 是否是第一次登陆
     */
    fun isFirst(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool("first", true)
    }

    /**
     * 是否是第一次登陆
     */
    fun setFirst(first:Boolean): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.encode("first", first)
    }
}