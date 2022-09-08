package com.my.path.ui.fragment.web

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import com.my.path.app.base.BaseFragment
import com.my.path.app.ext.initTitleWeb
import com.my.path.databinding.FragmentWebBinding
import com.my.path.viewmodel.state.web.WebViewModel
import com.just.agentweb.AgentWeb
import me.guangnian.mvvm.ext.nav

/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　: h5页面
 */
class WebFragment : BaseFragment<WebViewModel, FragmentWebBinding>() {

    private var mAgentWeb: AgentWeb? = null

    private var preWeb: AgentWeb.PreAgentWeb? = null

    override fun initView(savedInstanceState: Bundle?) {

        preWeb = AgentWeb.with(this)
            .setAgentWebParent(mDatabind.webcontent, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()

        arguments?.run {
            mViewModel.showTitle = getString("title","")
            mViewModel.url =  getString("url","")
        }

        mDatabind.includeToolbar.toolbar.initTitleWeb(mViewModel.showTitle) {
            mAgentWeb?.let { web ->
                if (web.webCreator.webView.canGoBack()) {
                    web.webCreator.webView.goBack()
                } else {
                    nav().navigateUp()
                }
            }
        }
    }

    override fun lazyLoadData() {
        //加载网页
        mAgentWeb = preWeb?.go(mViewModel.url)
        requireActivity().onBackPressedDispatcher.addCallback(this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    mAgentWeb?.let { web ->
                        if (web.webCreator.webView.canGoBack()) {
                            web.webCreator.webView.goBack()
                        } else {
                            nav().navigateUp()
                        }
                    }
                }
            })
    }

    override fun createObserver() {

    }

    override fun onPause() {
        mAgentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb?.webLifeCycle?.onDestroy()
        mActivity.setSupportActionBar(null)
        super.onDestroy()
    }

}