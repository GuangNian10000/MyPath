package com.my.path.ui.adapter.home

import android.annotation.SuppressLint
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.my.path.R
import com.my.path.app.config.SysConfig.BookRounded
import com.my.path.app.ext.*
import com.my.path.app.util.CacheUtil
import com.my.path.app.util.CacheUtil.getUserData
import com.my.path.app.util.ImageUtil.loadRoundImage
import com.my.path.app.weight.textview.VerticalTextView
import com.my.path.data.model.bean.BaseData
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder

/**
@author GuangNian
@description:
@date : 4/23/21 3:35 PM
 */
class DataAdapter(val fragment: Fragment): BaseBannerAdapter<BaseData>()  {

    @SuppressLint("SetTextI18n")
    override fun bindData(
        holder: BaseViewHolder<BaseData>,
        data: BaseData,
        position: Int,
        pageSize: Int
    ) {

        val conData = holder.findViewById<ConstraintLayout>(R.id.conData)
        val conSy = holder.findViewById<ConstraintLayout>(R.id.conSy)
        val conXfjl = holder.findViewById<ConstraintLayout>(R.id.conXfjl)

        val imageBook = holder.findViewById<ImageView>(R.id.bookCover)
        val bookName = holder.findViewById<TextView>(R.id.tvBookName)
        val tvZzm = holder.findViewById<TextView>(R.id.tvZzm)

        val tvjrtjp = holder.findViewById<TextView>(R.id.tvjrtjp)
        val tvZrlll = holder.findViewById<TextView>(R.id.tvZrlll)
        val tvJrscl = holder.findViewById<TextView>(R.id.tvJrscl)

        val tvBydy = holder.findViewById<TextView>(R.id.tvBydy)
        val tvBylw = holder.findViewById<TextView>(R.id.tvBylw)

        val tvSygf = holder.findViewById<TextView>(R.id.tvSygf)
        val tvLjgfs = holder.findViewById<TextView>(R.id.tvLjgfs)

        val bookNameCover = holder.findViewById<VerticalTextView>(R.id.bookNameCover)
        val tvUserName = holder.findViewById<VerticalTextView>(R.id.tvUserName)
        bookNameCover.setTextSizeSp(8f)
        tvUserName.setTextSizeSp(6f)

        if(""!=data.cover){
            fragment.requireContext().loadRoundImage(data.cover, imageBook, BookRounded)
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
            fragment.requireContext().loadRoundImage(R.drawable.ic_morenstudawesd, imageBook, BookRounded)
        }


//        if(""!=data.cover){
//            fragment.requireContext().loadRoundImage(data.cover, imageBook, BookRounded)
//        }

        bookName.text = data.name

        tvZzm.text = getUserData()?.nick

        tvjrtjp.text = strToIntNULL(data.ticket.toString())
        tvZrlll.text = strToIntNULL(data.hits.toString())
        tvJrscl.text = strToIntNULL(data.bookshelf.toString())

        tvBydy.text =strToIntNULL( data.subscribe.toString())
        tvBylw.text = strToIntNULL(data.gift.toString())

        tvSygf.text =strToIntNULL( data.lastin.toString())
        tvLjgfs.text = strToIntNULL(data.totalin.toString())

        conData.clickWithDebounce {
            fragment.inDataSituationFragment(data)
        }

        conSy.clickWithDebounce {
            fragment.inDataEarningsFragment(data)
        }

        conXfjl.clickWithDebounce {
            fragment.inDataPaymentFragment(data)
        }
    }

    override fun getLayoutId(viewType: Int): Int =
        R.layout.item_writing_data

}