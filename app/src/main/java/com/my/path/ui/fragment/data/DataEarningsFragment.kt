package com.my.path.ui.fragment.data

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.config.SysConfig
import com.my.path.app.ext.clickWithDebounce
import com.my.path.app.ext.getBackgroundExt
import com.my.path.app.util.CacheUtil
import com.my.path.app.util.ImageUtil.loadGaussianImage
import com.my.path.app.util.ImageUtil.loadRoundImage
import com.my.path.data.model.bean.*
import com.my.path.databinding.FragmentHomeDataEarningsBinding
import com.my.path.ui.dialog.SwitchBookDialog
import com.my.path.viewmodel.request.data.RequestDataViewModel
import com.my.path.viewmodel.state.data.DataSituationViewModel
import com.lxj.xpopup.XPopup
import me.guangnian.mvvm.ext.parseState


/**
 * @author GuangNian
 * @description:
 * @date : 2022/2/17 11:19 上午
 */
class DataEarningsFragment : BaseFragment<DataSituationViewModel, FragmentHomeDataEarningsBinding>(){
    private val requestDataViewModel: RequestDataViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {

        addLoadingObserve(requestDataViewModel)

        mDatabind.viewmodel = mViewModel

        mDatabind.click = ProxyClick()

        arguments?.run {
            getParcelable<BaseData>("baseData")?.let {
                mViewModel.baseData .set(it)
                requestDataViewModel.dataSubscribe(it.bid)
                requestDataViewModel.dataGift(it.bid)
                requestDataViewModel.dataSubscribeDetail(it.bid,true)
                setBookNull(it.cover,it.name)

            }
        }

        mDatabind.viewDataSituation.constraintLayout6.clickWithDebounce {
            ProxyClick().swBook()
        }

        mDatabind.viewDataSituation.frameLayout5.clickWithDebounce {
            ProxyClick().swBook()
        }

        mDatabind.viewDataSituation.imagekk.clickWithDebounce {
            ProxyClick().swBook()
        }
    }

    override fun createObserver() {
        requestDataViewModel.dataSubscribeDetailResult.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                nullLayout(false)
                //成功
                when {
                    //第一页并没有数据 显示空布局界面
                    it.isFirstEmpty -> {
                        nullLayout(true)
                    }
                    //是第一页
                    it.isRefresh -> {
                        mDatabind.llContent.removeAllViews()
                        addListData(it.listData)
                    }
                    //不是第一页
                    else -> {
                        //没有数据
                        if(it.listData.size<=0){
                            nullLayout(true)
                        }
                        addListData(it.listData)
                    }
                }
            } else {
                //失败
                nullLayout(true)
            }
        })

        requestDataViewModel.dataGiftResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                mViewModel.dataGift.set(it)
            })
        }

        requestDataViewModel.dataSubscribeResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                mViewModel.dataSub.set(it)
            })
        }


        requestDataViewModel.dataBaseResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {dataBase->
                mViewModel.baseData.get()?.let {
                    dataBase.data.forEach {
                        if(it.bid==it.bid){
                            it.select = true
                        }
                    }
                    XPopup.Builder(context)
                        .asCustom(SwitchBookDialog(requireActivity(),dataBase.data){ book->
                            mViewModel.baseData.set(book)
                            requestDataViewModel.dataSubscribe(book.bid)
                            requestDataViewModel.dataGift(book.bid)
                            requestDataViewModel.dataSubscribeDetail(book.bid,true)
                            setBookNull(book.cover,book.name)
                        }).show()
                }

            })
        }
    }

    inner class ProxyClick {
        fun swBook(){
            requestDataViewModel.dataBase()
        }

        fun addMore(){
            mViewModel.baseData.get()?.let {
                requestDataViewModel.dataSubscribeDetail(it.bid,false)
            }
        }
    }

    //添加数据
    @SuppressLint("SetTextI18n")
    private fun addListData(listData:ArrayList<DataSubscribe>){
        listData.forEach {
            val view = View.inflate(activity, R.layout.item_data_earnings, null)
            view.findViewById<TextView>(R.id.tvChapter).text = it.name
            view.findViewById<TextView>(R.id.tvSub).text = it.count
            view.findViewById<TextView>(R.id.tvPrice).text = it.amount+getString(R.string.xingkongBiadwe)
            mDatabind.llContent.addView(view)
        }
    }

    //数据显示隐藏
    private fun nullLayout(isNull:Boolean){
        if(isNull){
            mDatabind.tvNull.visibility = View.VISIBLE
            mDatabind.tvMore.visibility = View.GONE
        }else{
            mDatabind.tvNull.visibility = View.GONE
            mDatabind.tvMore.visibility = View.VISIBLE
        }
    }

    fun setBookNull(cover:String,bookName:String){
        val tvUserName = mDatabind.viewDataSituation.viewBookCover.tvUserName
        val bookNameCover = mDatabind.viewDataSituation.viewBookCover.bookNameCover
        val imageBook =  mDatabind.viewDataSituation.viewBookCover.bookCover
        bookNameCover.setTextSizeSp(8f)
        tvUserName.setTextSizeSp(6f)
        if(""!=cover){
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
            requireContext().loadRoundImage(cover, imageBook, SysConfig.BookRounded)
            requireContext().loadGaussianImage(cover,  mDatabind.viewDataSituation.imagebg)
        }else{
            tvUserName.post {
                val text = CacheUtil.getUserData()?.author+" 著"
                val bool = tvUserName.canFillFull(text)
                if(bool){
                    tvUserName.setText(text)
                }
            }
            bookNameCover.post {
                val bool = bookNameCover.canFillFull(bookName)
                if(bool){
                    bookNameCover.setText(bookName)
                }
            }
            requireContext().loadRoundImage(R.drawable.ic_morenstudawesd, imageBook,
                SysConfig.BookRounded
            )
            mDatabind.viewDataSituation.imagebg.setImageDrawable(getBackgroundExt(R.drawable.bg_moren_book_s))
        }

    }
}