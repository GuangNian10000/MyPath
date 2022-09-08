package com.my.path.ui.fragment.message

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.config.SysConfig
import com.my.path.app.ext.*
import com.my.path.data.interfaces.PopupShieldingCallback
import com.my.path.data.model.bean.TipGiftData
import com.my.path.databinding.*
import com.my.path.ui.dialog.BookTagDialog
import com.my.path.viewmodel.request.RequestMeViewModel
import com.my.path.viewmodel.request.message.RequestMessageViewModel
import com.my.path.viewmodel.state.message.MessageListViewModel
import com.drake.brv.utils.*
import com.kingja.loadsir.core.LoadService
import com.lxj.xpopup.XPopup
import me.guangnian.mvvm.base.appContext

/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　: h5页面
 */
class GiftListFragment : BaseFragment<MessageListViewModel, FragmentTicketListBinding>() {
    private lateinit var loadsir: LoadService<Any>

    private val messageRequestViewModel: RequestMessageViewModel by viewModels()

    private val meRequestViewModel: RequestMeViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel

        //状态页配置
        loadsir = loadServiceInit(mDatabind.page) {
            //点击重试时触发的操作
            loadsir.showLoading()
            meRequestViewModel.meGetbook()
        }

        mDatabind.rv.linear().divider(R.drawable.bg_fengexian).setup {
            addType<TipGiftData>(R.layout.item_gift_list)
            onBind {
                findView<ImageView>(R.id.imageGd).visibility = View.GONE
            }
            R.id.imageGd.onClick {
                getModelOrNull<TipGiftData>()?.let {
                    showShieldingDialog(it.uinfo._id,modelPosition,object :
                        PopupShieldingCallback {
                        override fun shielding(uid: Int, pos: Int) {
                            mDatabind.page.refresh()
                        }
                    })
                }

            }
        }

        // 下拉刷新
        mDatabind.page.onRefresh {
            messageRequestViewModel.tipGift(mViewModel.currentBid.get(),index)
        }

        mDatabind.page.onLoadMore {
            messageRequestViewModel.tipGift(mViewModel.currentBid.get(),index)
        }

        meRequestViewModel.meGetbook()

        mDatabind.viewMessageBookTop.showBook.clickWithDebounce {
            XPopup.Builder(appContext)
                .atView(mDatabind.viewMessageBookTop.title)
                .moveUpToKeyboard(false)
                .asCustom(BookTagDialog(requireActivity(),mViewModel.currentBid.get()){
                    mViewModel.currentBid.set(it._id)
                    mViewModel.currentTitle.set(it.title)
                    mDatabind.page.refresh()})
                .show()
        }
    }

    override fun createObserver() {
        meRequestViewModel.meGetbookResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState,loadsir, {
                if(it.books.size>0){
                    val data = it.books[0]
                    mViewModel.currentBid.set(data._id)
                    mViewModel.currentTitle.set(data.title)
                    mDatabind.page.refresh()
                    mDatabind.viewMessageBookTop.title.visibility = View.VISIBLE
                }else{
                    mDatabind.viewMessageBookTop.title.visibility = View.GONE
                    loadsir.showEmpty(SysConfig.NUll_MESSAGE_LIST)
                }
            },{}, SysConfig.NUll_MESSAGE_LIST)
        }

        messageRequestViewModel.tipGiftResult.observe(viewLifecycleOwner, Observer {
            it.listData.forEach {
                it.bookName = mViewModel.currentTitle.get()
            }
            mDatabind.page.addData(it.listData,mDatabind.rv.bindingAdapter,{it.isEmpty},{it.hasMore})
        })
    }

}