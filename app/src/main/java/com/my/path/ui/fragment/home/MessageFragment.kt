package com.my.path.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.my.path.app.base.BaseFragment
import com.my.path.app.ext.*
import com.my.path.databinding.FragmentMessageBinding
import com.my.path.viewmodel.request.message.RequestMessageViewModel
import com.my.path.viewmodel.state.home.MessageViewModel
import me.guangnian.mvvm.ext.parseState


/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　: h5页面
 */
class MessageFragment : BaseFragment<MessageViewModel, FragmentMessageBinding>() {

    private val messageRequestViewModel: RequestMessageViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel
        mDatabind.proxyClick = ProxyClick()

        //初始化 SwipeRefreshLayout
        mDatabind.swipeRefresh.init {
            //触发刷新监听时请求数据
            messageRequestViewModel.tipIndex()
        }

//        messageRequestViewModel.tipIndex()
    }

    inner class ProxyClick {
        fun tipReadall(){
            messageRequestViewModel.tipReadall()
        }
        fun inTicketList(){
            inTicketListFragment()
        }

        fun inRedList(){
            inRedListFragment()
            //inTicketListFragment()
        }

        fun inGiftList(){
            inGiftListFragment()
            //inTicketListFragment()
        }

        fun inCommentList(){
            inCommentListFragment()
//            eventViewModel.inAppFragment.value = InApp(1,0,67535,0)
            //inTicketListFragment()
        }
    }

    override fun createObserver() {
        messageRequestViewModel.tipReadallResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                messageRequestViewModel.tipIndex()
            }, {
                //showMessage(it.errorMsg)
            })
        }

        messageRequestViewModel.tipIndexResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, mDatabind.swipeRefresh,{
                mViewModel.tipIndex.set(it)
            },{})
        }

        messageRequestViewModel.tipReadallResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                messageRequestViewModel.tipIndex()
            }, {})
        }
    }


    override fun onResume() {
        super.onResume()
        messageRequestViewModel.tipIndex()
    }
}