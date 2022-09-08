package com.my.path.ui.fragment.min

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.ext.*
import com.my.path.app.util.*
import com.my.path.databinding.FragmentSetShieldingBinding
import com.my.path.ui.adapter.home.ShieldingAdapter
import com.my.path.ui.adapter.home.spaces.SpaceItemDecoration
import com.my.path.viewmodel.request.ShieldingRequestViewModel
import com.my.path.viewmodel.state.ShieldingViewModel
import com.kingja.loadsir.core.LoadService
import me.guangnian.mvvm.ext.nav
import me.guangnian.mvvm.ext.parseState
import me.guangnian.mvvm.ext.util.dp2px

/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　: 设置
 */
class ShieldingFragment : BaseFragment<ShieldingViewModel, FragmentSetShieldingBinding>() {
    private lateinit var loadsir: LoadService<Any>

    private val shieldingRequestViewModel: ShieldingRequestViewModel by viewModels()

    private val shieldingAdapter: ShieldingAdapter by lazy { ShieldingAdapter(arrayListOf()) }

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(shieldingRequestViewModel)
        mDatabind.viewmodel = mViewModel
        mDatabind.click = ProxyClick()

        mDatabind.include.toolbar.initTitle(getString(R.string.hbsadawe)) {
            nav().navigateUp()
        }

        //状态页配置
        loadsir = loadServiceInit(mDatabind.includeList.includeRecyclerview.swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showLoading()
            shieldingRequestViewModel.flowBlacklist(true)
        }

        //初始化 SwipeRefreshLayout
        mDatabind.includeList.includeRecyclerview.swipeRefresh.init {
            //触发刷新监听时请求数据
            shieldingRequestViewModel.flowBlacklist(true)
        }

        mDatabind.includeList.includeRecyclerview.recyclerView.init(LinearLayoutManager(context), shieldingAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, requireContext().dp2px(8f)))

        }

        shieldingAdapter.run {
            addChildClickViewIds(R.id.jiechupnbi)
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.jiechupnbi -> {
                        shieldingRequestViewModel.flowRemoveblacklist(position,shieldingAdapter.data[position]._id)
                    }
                }
            }
        }
    }

    override fun createObserver() {
        shieldingRequestViewModel.flowBlacklistResult.observe(viewLifecycleOwner, Observer {
            //设值 新写了个拓展函数，搞死了这个恶心的重复代码
            loadListData2(it,
                shieldingAdapter,
                loadsir,
                mDatabind.includeList.includeRecyclerview.recyclerView,
                mDatabind.includeList.includeRecyclerview.swipeRefresh
            )
        })

        shieldingRequestViewModel.flowRemoveblacklistResult.observe(viewLifecycleOwner, Observer { resultState ->
            parseState(resultState, {
                shieldingAdapter.data.removeAt(it.position)
                shieldingAdapter.notifyItemRemoved(it.position)
            },{
            })
        })
    }

    inner class ProxyClick {
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        shieldingRequestViewModel.flowBlacklist(true)
    }
}