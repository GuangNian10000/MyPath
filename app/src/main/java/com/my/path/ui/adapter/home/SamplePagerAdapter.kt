package com.my.path.ui.adapter.home

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.my.path.data.model.bean.ItemOnClick
import com.lxj.xpopup.photoview.PhotoView
import me.guangnian.mvvm.ext.nav
import java.util.*

/**
 * @author GuangNian
 * @description:
 * @date : 2021/8/2 11:38 上午
 */
class SamplePagerAdapter(val activity:Fragment,var images : ArrayList<String>,var currentPosition :Int,val onClick: ItemOnClick) : PagerAdapter(), ViewPager.OnPageChangeListener {
    override fun getCount(): Int =
        images.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean{
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val photoView = PhotoView(container.context)
        try {
            Glide.with(activity).load(images[position].replace("_s.",".")).into(photoView)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        container.addView(
            photoView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        photoView.setOnPhotoTapListener { view, x, y ->  activity.nav().navigateUp()}
        return photoView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        onClick.OnClick(position)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }
}