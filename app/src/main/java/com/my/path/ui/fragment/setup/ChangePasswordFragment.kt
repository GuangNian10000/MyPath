package com.my.path.ui.fragment.setup

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.ext.hideSoftKeyboard
import com.my.path.app.ext.initTitle
import com.my.path.app.ext.showMessage
import com.my.path.app.util.*
import com.my.path.databinding.FragmentChangePasswordBinding
import com.my.path.viewmodel.request.ChangePasswordRequestViewModel
import com.my.path.viewmodel.state.ChangePasswordViewModel
import me.guangnian.mvvm.ext.nav
import me.guangnian.mvvm.ext.parseState

/**
 * @author　: GuangNian
 * @date　: 2019/12/24
 * @description　:
 */
class ChangePasswordFragment : BaseFragment<ChangePasswordViewModel, FragmentChangePasswordBinding>() {

    private val requestLoginRegisterViewModel: ChangePasswordRequestViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel
        mDatabind.click = ProxyClick()

        mDatabind.includeToolbar.toolbar.initTitle(context?.getString(R.string.xiugaimiaasdx)) {
            nav().navigateUp()
        }
    }

    override fun createObserver() {
        requestLoginRegisterViewModel.meEditpassResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                nav().navigateUp()
            }, {
                showMessage(it.errorMsg)
            })
        }
    }


    inner class ProxyClick {

        fun goLogin(){
            hideSoftKeyboard(activity)
            val pawd1 = mViewModel.pawd1.get()
            val pawd2 = mViewModel.pawd2.get()
            if (pawd1 == ""||pawd2=="") {
                showMessage(getString(R.string.mimabunengwieskoasd))
                return
            }
            requestLoginRegisterViewModel.meEditpass(pawd1,pawd2)
        }

        fun shutDown(){
            hideSoftKeyboard(activity)
            nav().navigateUp()
        }
    }
}