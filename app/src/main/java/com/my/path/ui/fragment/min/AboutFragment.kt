package com.my.path.ui.fragment.min

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.config.SysConfig
import com.my.path.app.ext.getVersionName
import com.my.path.app.ext.inWebFragment
import com.my.path.app.ext.initTitle
import com.my.path.app.util.*
import com.my.path.databinding.FragmentAboutBinding
import com.my.path.viewmodel.request.AboutRegisterViewModel
import com.my.path.viewmodel.state.AboutViewModel
import me.guangnian.mvvm.ext.nav

/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　: 关于我们
 */
class AboutFragment : BaseFragment<AboutViewModel, FragmentAboutBinding>() {
    private val aboutRegisterViewModel: AboutRegisterViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(aboutRegisterViewModel)
        mDatabind.viewmodel = mViewModel
        mDatabind.click = ProxyClick()


        mDatabind.include.toolbar.initTitle(context?.getString(R.string.guanyuwomasdx)) {
            nav().navigateUp()
        }
      //  initTitle(context?.getString(R.string.guanyuwomasdx))

        mViewModel.version.set(getString(R.string.banbenhaosadx)+ getVersionName(activity))
    }

    override fun createObserver() {

    }

    inner class ProxyClick {
        fun toUserService(){
            inWebFragment(SysConfig.FWUXIY)
        }

        fun toPrivacy(){
            inWebFragment(SysConfig.YINSI)
        }
    }
}