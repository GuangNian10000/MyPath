package com.my.path.ui.adapter.home

import android.annotation.SuppressLint
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.my.path.R
import com.my.path.app.config.SysConfig.BookRounded
import com.my.path.app.ext.*
import com.my.path.app.util.CacheUtil
import com.my.path.app.util.ImageUtil.loadGaussianImage
import com.my.path.app.util.ImageUtil.loadRoundImage
import com.my.path.app.weight.textview.VerticalTextView
import com.my.path.data.model.bean.Novel
import com.my.path.ui.dialog.BookMoreDialog
import com.lxj.xpopup.XPopup
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder


/**
@author GuangNian
@description:
@date : 4/23/21 3:35 PM
 */
class WritingAdapter(val fragment: Fragment,private val onCallback :((Int,Novel) -> Unit)): BaseBannerAdapter<Novel>()  {
    val mTypeList  = mapOf( 0 to "" ,
        2 to fragment.requireContext().getString(R.string.shenghezhonasd),
        3 to fragment.requireContext().getString(R.string.weishengheasd) ,
        4 to  fragment.requireContext().getString(R.string.tongupashcawd) ,
        5 to fragment.requireContext().getString(R.string.jingzhidawddawd))

    @SuppressLint("SetTextI18n")
    override fun bindData(
        holder: BaseViewHolder<Novel>,
        data: Novel,
        position: Int,
        pageSize: Int
    ) {
        val frameLayoutBook = holder.findViewById<FrameLayout>(R.id.frameLayoutBook)
        val imageBook = holder.findViewById<ImageView>(R.id.bookCover)
        val bookName = holder.findViewById<TextView>(R.id.tvBookName)
        val tvState = holder.findViewById<TextView>(R.id.tvState)
        val tvBjZj = holder.findViewById<TextView>(R.id.tvBjZj)
        val tvMore = holder.findViewById<TextView>(R.id.tvGdGN)
        val bookBg = holder.findViewById<ImageView>(R.id.bookBg)

        val bookNameCover = holder.findViewById<VerticalTextView>(R.id.bookNameCover)
        val tvUserName = holder.findViewById<VerticalTextView>(R.id.tvUserName)
        bookNameCover.setTextSizeSp(18f)
        tvUserName.setTextSizeSp(12f)


        if(""!=data.cover){
            fragment.requireContext().loadRoundImage(data.cover, imageBook, BookRounded)
            fragment.requireContext().loadGaussianImage(data.cover, bookBg)
            tvUserName.post {
                val bool = tvUserName.canFillFull( "")
                if(bool){
                    tvUserName.setText("")
                }
            }
            bookNameCover.post {
                val bool = bookNameCover.canFillFull( "")
                if(bool){
                    bookNameCover.setText("")
                }
            }
        }else{
            tvUserName.post {
                val text = CacheUtil.getUserData()?.author+" 著"
                val bool = tvUserName.canFillFull(text)
                if(bool){
                    tvUserName.setText(text)
                }
            }
            bookNameCover.post {
                val bool = bookNameCover.canFillFull(data.name)
                if(bool){
                    bookNameCover.setText(data.name)
                }
            }

            bookBg.setImageDrawable(getBackgroundExt(R.drawable.bg_moren_book))
            fragment.requireContext().loadRoundImage(R.drawable.ic_morenstudawesd, imageBook, BookRounded)
            //fragment.requireContext().loadGaussianImage(R.drawable.ic_morenstudawesd, bookBg)
            //loadRoundImage(context,R.drawable.ic_book, imageBook, 12)
        }


        bookName.text = data.name
        //0正常,2审核中,3未审核,4通过初审,5静止提交审核
        if(mTypeList.containsKey(data.type)){
            tvState.text = mTypeList[data.type]
            tvState.visibility = View.VISIBLE
        }else{
            tvState.visibility = View.GONE
        }

        tvBjZj.clickWithDebounce {
            fragment.inChapterListFragment(data)
        }

        tvMore.clickWithDebounce {
            XPopup.Builder(fragment.requireContext())
                .asCustom(BookMoreDialog(fragment.requireActivity()){
                    onCallback.invoke(it,data)
                })
                .show()
        }
    }

    override fun getLayoutId(viewType: Int): Int =
        R.layout.item_writing_book
}