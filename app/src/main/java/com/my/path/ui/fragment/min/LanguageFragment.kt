package com.my.path.ui.fragment.min

import android.os.Bundle
import android.view.View
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.ext.getLanguage
import com.my.path.app.ext.initTitle
import com.my.path.app.ext.setLanguage
import com.my.path.app.util.*
import com.my.path.app.util.SettingUtil.isSystemLanguages
import com.my.path.databinding.FragmentSetLanguageBinding
import com.my.path.viewmodel.state.SetUpViewModel
import me.guangnian.mvvm.ext.nav

/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　: 设置
 */
class LanguageFragment : BaseFragment<SetUpViewModel, FragmentSetLanguageBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel
        mDatabind.click = ProxyClick()


        mDatabind.include.toolbar.initTitle(getString(R.string.yuyanshehiase)) {
            nav().navigateUp()
        }

        UISwitch()

    }

    override fun createObserver() {

    }

    inner class ProxyClick {
        fun a(){

            context?.let {
                SettingUtil.setIsSystemLanguages(it,true)
                it.setLanguage(it.getLanguage())
            }
        }

        fun b(){
            context?.let {
                SettingUtil.setIsSystemLanguages(it,false)
                it.setLanguage(true)
            }


        }

        fun c(){
//
            context?.let {
                SettingUtil.setIsSystemLanguages(it,false)
                it.setLanguage(false)
            }
        }
    }

    /**
     * UI切换
     * */
    private fun UISwitch(){
        var type = 0
        //初始化UI
        context?.let {
            type =  when {
                isSystemLanguages(it) -> {
                    //是否跟随系统
                    0
                }
                it.getLanguage() -> {
                    //简体
                    1
                }
                else -> {
                    2
                }
            }
        }

        when(type){
            0->{
                mDatabind.image1.visibility= View.VISIBLE
                mDatabind.image2.visibility= View.GONE
                mDatabind.image3.visibility= View.GONE
            }
            1->{
                mDatabind.image1.visibility= View.GONE
                mDatabind.image2.visibility= View.VISIBLE
                mDatabind.image3.visibility= View.GONE
            }
            2->{
                mDatabind.image1.visibility= View.GONE
                mDatabind.image2.visibility= View.GONE
                mDatabind.image3.visibility= View.VISIBLE
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        UISwitch()
    }
}