package com.my.path.ui.fragment.message

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.my.path.app.base.BaseFragment
import com.my.path.app.ext.clickWithDebounce
import com.my.path.data.model.bean.ItemOnClick
import com.my.path.databinding.FragmentPictureViewerBinding
import com.my.path.ui.adapter.home.SamplePagerAdapter
import com.my.path.viewmodel.state.home.MeViewModel
import com.lxj.xpopup.widget.HackyViewPager

import me.guangnian.mvvm.ext.nav

/**
 * @author GuangNian
 * @description:
 * @date : 2022/4/22 4:10 下午
 */
class PictureViewerFragment : BaseFragment<MeViewModel, FragmentPictureViewerBinding>() {
    private var mViewPager: ViewPager?=null
    private var images :ArrayList<String>? = ArrayList()
    private var currentPosition = 0

    override fun initView(savedInstanceState: Bundle?) {
        mViewPager = HackyViewPager(requireContext())

        arguments?.run {
            images = getStringArrayList("images")
            currentPosition = getInt("currentPosition", 0)

        }

        images?.let {
            if (it.size == 1) {
                mDatabind.ivLoad.visibility = View.GONE
            }
        }

    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        mDatabind.imageBack.clickWithDebounce {
            nav().navigateUp()
        }

        mDatabind.pPhoto.addView(mViewPager)
        images?.let {
            val samplePagerAdapter = SamplePagerAdapter(this,it,currentPosition,object :
                ItemOnClick {
                override fun OnClick(result: Any) {
                    val pos = it + 1
                    mDatabind.tvCount.text = pos.toString()+"/" + it.size
                }
            })
            mViewPager?.adapter = samplePagerAdapter
            mViewPager?.setCurrentItem(currentPosition, false)
            mViewPager?.addOnPageChangeListener(samplePagerAdapter)
        }

    }
}