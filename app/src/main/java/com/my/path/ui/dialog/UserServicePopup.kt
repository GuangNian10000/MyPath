package com.my.path.ui.dialog

import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.my.path.R
import com.my.path.app.config.SysConfig
import com.my.path.app.ext.clickWithDebounce
import com.lxj.xpopup.core.CenterPopupView
import me.jessyan.retrofiturlmanager.RetrofitUrlManager

/**
 * @author GuangNian
 * @description:
 * @date : 2021/5/28 2:53 下午
 */
class UserServicePopup(val fragment: Fragment,  private val onCallback :((Boolean) -> Unit)) : CenterPopupView(fragment.requireContext()) {


    override fun getImplLayoutId(): Int = R.layout.popup_window_service_agreement2

    override fun onCreate() {
        super.onCreate()

        loadURL()

        findViewById<Button>(R.id.bt_yes).clickWithDebounce {
            onCallback.invoke(true)
            dismiss()
        }

        findViewById<TextView>(R.id.tv_on).clickWithDebounce {
            onCallback.invoke(false)
            dismiss()
        }
    }

    fun loadURL() {
        val webView = findViewById<WebView>(R.id.webView)
        webView.loadUrl( RetrofitUrlManager.getInstance().globalDomain.toString() + SysConfig.yinSiUrl)
        val settings: WebSettings = webView.getSettings()
        settings.setAppCacheEnabled(false)
        settings.setJavaScriptEnabled(true)
        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                view.loadUrl(url!!)
                return true
            }
        })
        webView.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress == 100) {
                    // 加载完成
                } else {
                    // 加载中
                }
            }
        })
    }
}