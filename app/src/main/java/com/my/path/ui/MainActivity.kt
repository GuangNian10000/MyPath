package com.my.path.ui

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import com.my.path.R
import com.my.path.app.base.BaseActivity
import com.my.path.app.eventViewModel
import com.my.path.app.ext.inBrowser
import com.my.path.app.ext.makeToast
import com.my.path.databinding.ActivityMainBinding
import com.my.path.viewmodel.state.LoginViewModel
import me.guangnian.mvvm.ext.util.toJson
import me.jessyan.autosize.AutoSize


class MainActivity : BaseActivity<LoginViewModel, ActivityMainBinding>() {

    var exitTime = 0L

    override fun initView(savedInstanceState: Bundle?) {
        AutoSize.autoConvertDensity(this,360f,true)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val nav = Navigation.findNavController(this@MainActivity, R.id.host_fragment)

                if (nav.currentDestination != null && nav.currentDestination!!.id == R.id.chapterAddFragment){
                    eventViewModel.onSaveChapter.value = true
                } else if (nav.currentDestination != null && nav.currentDestination!!.id != R.id.mainfragment
                    && nav.currentDestination!!.id != R.id.loginFragment) {
                    //如果当前界面不是主页，那么直接调用返回即可
                    nav.navigateUp()
                } else {
                    //是主页
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        makeToast("再按一次退出程序")
                        exitTime = System.currentTimeMillis()
                    } else {
                        finish()
                    }
                }
            }
        })

        eventViewModel.inAppFragment.observeInActivity(this){
            try {
                val componetName = ComponentName( //这个是另外一个应用程序的包名eg:com.shuto.mobile
                    "com.sdt.dlxk",  //这个参数是要启动的Activity eg:com.shuto.mobile.ui.LoginActivity
                    "com.sdt.dlxk.ui.activity.MainActivity")
                val intent = Intent()
                intent.component = componetName
                intent.putExtra("inApp", it.toJson())
                startActivity(intent)

                //intent.action = "com.dlxk.st.inapp"
                //intent.putExtra("inApp", it.toJson())
              //  startActivity(intent)
            }catch ( e: ActivityNotFoundException) {
                inBrowser()
            } catch ( e:Exception) {
                inBrowser()
            }

        }
    }
}