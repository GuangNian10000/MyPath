package com.my.path.ui.fragment.setup

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.ext.clickWithDebounce
import com.my.path.app.ext.initTitle
import com.my.path.app.ext.showMessage
import com.my.path.data.model.bean.UserData

import com.my.path.databinding.ActivityAccountSecurityBinding
import com.my.path.viewmodel.request.RequestLoginRegisterViewModel

import com.my.path.viewmodel.state.AccountSecurityViewModel
import com.my.path.viewmodel.state.RequestMinViewModel
import me.guangnian.mvvm.ext.nav
import me.guangnian.mvvm.ext.navigateAction
import me.guangnian.mvvm.ext.parseState

/**
 * @author GuangNian
 * @description:
 * @date : 2022/2/24 4:19 下午
 */
class AccountSecurityFragment : BaseFragment<AccountSecurityViewModel, ActivityAccountSecurityBinding>() {
    private val requestLoginRegisterViewModel: RequestLoginRegisterViewModel by viewModels()
    private val requestMinViewModel: RequestMinViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(requestLoginRegisterViewModel)
        addLoadingObserve(requestMinViewModel)
        mDatabind.viewmodel = mViewModel
        mDatabind.click = ProxyClick()

        mDatabind.includeToolbar.toolbar.initTitle(context?.getString(R.string.zhanghuyuaquansdhuaw)) {
            nav().navigateUp()
        }

        requestMinViewModel.meGetInFo()

        mDatabind.rlShouji.clickWithDebounce {
            nav().navigateAction(R.id.bindPhoneFragment)
        }

        mDatabind.rlYouxiang.clickWithDebounce {
            nav().navigateAction(R.id.bindEmailFragment)

        }

        mDatabind.rlXiugaimia.clickWithDebounce {
            nav().navigateAction(R.id.changePasswordFragment)
        }
    }

    override fun createObserver() {
        //用户信息
        requestMinViewModel.meGetInFoResult.observe(
            this
        ) { resultState ->
            parseState(resultState, {
                setMeInfo(it)
            }, {
                showMessage(it.errorMsg)
            })
        }
    }

    inner class ProxyClick {

    }

    fun setMeInfo(bean: UserData) {
        val phone = (if(null==bean.mobile || "" == bean.mobile) "0" else  bean.mobile) != "0"
        val email = (if(null==bean.email || "" == bean.email) "0" else  bean.email) != "0"
        val wx =  bean.wx!=0
        val qq = bean.qq!=0
        val faceBook = bean.facebook!=0
        val pass = bean.pass!=0

        if(pass){
            mDatabind.tvMimaMiaos.text = getString(R.string.tv_yishezhimima)
            mDatabind.imageMima.setImageDrawable(activity?.let { ContextCompat.getDrawable(it, R.drawable.ic_acoun_yibangd) })
            mDatabind.tvMima.text = getString(R.string.xiugainima)
        }else{
            mDatabind.tvMimaMiaos.text = getString(R.string.tv_weishezhimima)
            mDatabind.imageMima.setImageDrawable(activity?.let { ContextCompat.getDrawable(it, R.drawable.ic_accoun_weibangd) })
            mDatabind.tvMima.text = getString(R.string.tv_yshezhimima)
        }

        if(phone){
            if(bean.mobile.length>2){
                mDatabind.tvShoujimians.text = bean.mobile.substring(0,2)+"*******"+bean.mobile.substring(bean.mobile.length-2, bean.mobile.length)
            }
            mDatabind.imageShouji.setImageDrawable(activity?.let { ContextCompat.getDrawable(it, R.drawable.ic_acoun_yibangd) })
            mDatabind.tvShoujiBangd.text = getString(R.string.tv_genghuanlijibangd)
        } else{
            mDatabind.tvShoujimians.text = getString(R.string.bind_shouji_weibangd)
            mDatabind.imageShouji.setImageDrawable(activity?.let { ContextCompat.getDrawable(it, R.drawable.ic_accoun_weibangd) })
            mDatabind.tvShoujiBangd.text = getString(R.string.tv_lijibangd)
        }

        if(email){
            if(bean.email.length>2){
                mDatabind.tvYouxiang.text = bean.email.substring(0,2)+"*******"+bean.email.substring(bean.email.length-2, bean.email.length)
            }
            mDatabind.imageYouxiang.setImageDrawable(activity?.let { ContextCompat.getDrawable(it, R.drawable.ic_acoun_yibangd) })
            mDatabind.tvYouxiangBangd.text = getString(R.string.tv_genghuanlijibangd)
        } else{
            mDatabind.tvYouxiang.text = getString(R.string.wweibangdasd)
            mDatabind.imageYouxiang.setImageDrawable(activity?.let { ContextCompat.getDrawable(it, R.drawable.ic_accoun_weibangd) })
            mDatabind.tvYouxiangBangd.text = getString(R.string.tv_lijibangd)
        }
        mDatabind.rlShezhimima.setOnClickListener {
            //先绑定手机号
            if(!phone){
                showMessage(getString(R.string.xianbangdinshou))
                return@setOnClickListener
            }
            if(pass){
                nav().navigateAction(R.id.changePasswordFragment)
            }else{
                nav().navigateAction(R.id.setRetrievePasswordFragment)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requestMinViewModel.meGetInFo()
    }
}