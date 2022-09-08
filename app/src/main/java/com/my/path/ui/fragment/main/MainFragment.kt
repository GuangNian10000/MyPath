package com.my.path.ui.fragment.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.eventViewModel
import com.my.path.app.ext.*
import com.my.path.app.util.CacheUtil
import com.my.path.app.util.CacheUtil.setFirst
import com.my.path.databinding.FragmentMainBinding
import com.my.path.viewmodel.request.RequestMainViewModel
import com.my.path.viewmodel.state.MainViewModel
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.FirebaseApp

/**
 * 时间　: 2019/12/27
 * 作者　: GuangNian
 * 描述　:项目主页Fragment
 */
class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {
    val requestMainViewModel:RequestMainViewModel  by viewModels()
    override fun initView(savedInstanceState: Bundle?) {

        //初始化viewpager2
        mDatabind.mainViewpager.initMain(this)
        //初始化 bottomBar
        mDatabind.mainBottom.init{
            when (it) {
                R.id.menu_1 -> mDatabind.mainViewpager.setCurrentItem(0, false)
                R.id.menu_2 -> mDatabind.mainViewpager.setCurrentItem(1, false)
                R.id.menu_3 -> mDatabind.mainViewpager.setCurrentItem(2, false)
                R.id.menu_4 -> mDatabind.mainViewpager.setCurrentItem(3, false)
            }
        }
        mDatabind.mainBottom.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED

        mDatabind.mainBottom.interceptLongClick(R.id.menu_1,R.id.menu_2,R.id.menu_3,R.id.menu_4)

        eventViewModel.onInLogin.observeInFragment(this) { data ->
            if(!data){
                inLoginCodeFragment()
            }else{
                inLoginFragment()
            }
            //handler.postDelayed(runnable, 1000)
        }

        if(CacheUtil.isFirst()){
            showUserService {
                if(it){
                    setFirst(false)
                    requireActivity().requestPermissionSTORAGE()
                    check()
                }else{
                    requireActivity().finish()
                }
            }
        }else{
            check()
        }
    }

    override fun onResume() {
        super.onResume()
        if(!CacheUtil.isFirst()){
            check()
        }
    }

    private fun check(){
        CacheUtil.isLogin(this)
        if(CacheUtil.isLogin()){
            mDatabind.llContent.visibility = View.VISIBLE
            FirebaseApp.initializeApp(requireContext())
            requestMainViewModel.firebasePerformance()
        }else{
            mDatabind.llContent.visibility = View.GONE
        }
    }
}