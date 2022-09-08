package com.my.path.ui.fragment.login

import android.os.Bundle
import android.widget.ListView
import androidx.fragment.app.viewModels
import com.my.path.app.appViewModel
import com.my.path.app.base.BaseFragment
import com.my.path.app.ext.getLanguage
import com.my.path.app.util.SimulateNetAPI.getOriginalFundData
import com.my.path.app.weight.customview.QuickLocationBar
import com.my.path.data.model.bean.AreaCode
import com.my.path.databinding.FragmentAreaCodeBinding
import com.my.path.ui.adapter.home.CityAdapter
import com.my.path.ui.adapter.home.CityItemOnClick
import com.my.path.viewmodel.state.AreaCodeViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import me.guangnian.mvvm.ext.nav
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　:
 */
class AreaCodeFragment : BaseFragment<AreaCodeViewModel, FragmentAreaCodeBinding>() {

    private val areaCodeViewModel: AreaCodeViewModel by viewModels()

    private var cityAdapter: CityAdapter? = null
    private val list: MutableList<AreaCode> =ArrayList()


    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(areaCodeViewModel)
        mDatabind.viewmodel = mViewModel
        mDatabind.click = ProxyClick()

        cityAdapter = activity?.let {
            CityAdapter(it, list,object : CityItemOnClick {
                override fun onClick(result: AreaCode) {
                    appViewModel.userCountryCode.value = result.number.toString()
                    appViewModel.userCountryName.value = result.name.toString()
                    nav().navigateUp()
                }})
        }
        mDatabind.cityListSearch.adapter = cityAdapter
        mDatabind.quick.setOnTouchLitterChangedListener(LetterListViewListener(mDatabind.cityListSearch,
            cityAdapter!!
        ))


        list.clear()
        activity?.let {
            if(it.getLanguage()){
                list.addAll(
                    Gson().fromJson( getOriginalFundData(it,"re_region_cn.json")
                        ,
                        object : TypeToken<List<AreaCode?>?>() {}.type))
            }else{
                list.addAll(
                    Gson().fromJson( getOriginalFundData(it,"re_region_hk.json")
                        ,
                        object : TypeToken<List<AreaCode?>?>() {}.type))
            }
            cityAdapter?.initData()
            cityAdapter?.notifyDataSetChanged()
        }
    }

    override fun createObserver() {
    }

    inner class ProxyClick {
    }

    private class LetterListViewListener(var listView: ListView, var cityAdapter: CityAdapter) : QuickLocationBar.OnTouchLetterChangedListener {
        override fun touchLetterChanged(s: String?) {
            if (s == "#") {
                listView.setSelection(0)
            } else {
                val alphaIndexer: Map<String, Int?> = cityAdapter.cityMap
                if (alphaIndexer[s] != null) {
                    val position = alphaIndexer[s]
                    if (position != null) {
                        listView.setSelection(position)
                    }
                }
            }
        }
    }
}