package com.my.path.ui.fragment.min

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.viewModels
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.ext.initTitle
import com.my.path.app.ext.showMessage
import com.my.path.app.util.*

import com.my.path.data.model.bean.IntItemOnClick
import com.my.path.data.model.bean.Picture
import com.my.path.databinding.FragmentFeedbackBinding
import com.my.path.ui.adapter.spaces.SpacesDynamicItem
import com.my.path.ui.fragment.chapter.FeedbackListAdapter
import com.my.path.viewmodel.request.FeedBackRequestModel
import com.my.path.viewmodel.state.FeedBackViewModel
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.callback.SelectCallback
import com.huantansheng.easyphotos.models.album.entity.Photo
import me.guangnian.mvvm.ext.nav
import me.guangnian.mvvm.ext.parseState
import kotlin.collections.ArrayList

/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　: 意见反馈
 */
class FeedBackFragment : BaseFragment<FeedBackViewModel, FragmentFeedbackBinding>() {

    //适配器
    private var feedBackAdapter: FeedbackListAdapter?=null

    private val aboutRegisterViewModel: FeedBackRequestModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(aboutRegisterViewModel)
        mDatabind.viewmodel = mViewModel
        mDatabind.click = ProxyClick()

        mDatabind.include.toolbar.initTitle(context?.getString(R.string.yijianfanksadx)) {
            nav().navigateUp()
        }
      //  initTitle(context?.getString(R.string.yijianfanksadx))

        feedBackAdapter = activity?.let {
            FeedbackListAdapter(it,mViewModel.listPicture,object : IntItemOnClick {
                override fun onClick(result: Int) {
                    clickImage(result)
                }
            })
        }

        mDatabind.recyclerViewPicture.adapter = feedBackAdapter
        mDatabind.recyclerViewPicture.isNestedScrollingEnabled = false
        activity?.let {
            mDatabind.recyclerViewPicture.addItemDecoration(SpacesDynamicItem(it))
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun createObserver() {
        //图片上传
        aboutRegisterViewModel.feedbackAddResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                mViewModel.etDescribe.set("")
                mViewModel.listPicture.clear()
                mViewModel.listPicture.add(Picture(true,null))
                mViewModel.theTextLength.set("0")
                feedBackAdapter?.notifyDataSetChanged()
                if(null!=it.result){
                    showMessage(it.result.toString())
                }else{
                    showMessage(getString(R.string.gandinawsd))
                }
            }, {
                showMessage(it.errorMsg)
            })
        }
        //图片上传
        aboutRegisterViewModel.feedbackUpimgResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
//                val list = it
//                mViewModel.listPicture.clear()
//                mViewModel.listPicture.addAll(list)
                feedBackAdapter?.notifyDataSetChanged()
            }, {
                showMessage(it.errorMsg)
            })
        }

    }

    inner class ProxyClick {
        //提交
        fun submit(){
            //图片
            var photo = ""
            //判断有没有上传失败的图
            for((i, s) in mViewModel.listPicture.withIndex()){
                if(s.isChoose){
                    continue
                }

                if(null==s.serviceUrl){
                    showMessage(getString(R.string.yicansgdtu))
                    return
                }
                photo += if(i==0){
                    s.serviceUrl
                }else{
                    ",${s.serviceUrl}"
                }
            }

            //判断数据不为空
            val content = mViewModel.etDescribe.get()
            if(""!=content){
                aboutRegisterViewModel.feedbackAdd(mViewModel.etDescribe.get(),photo)
            }else{
                showMessage(getString(R.string.bunengshendoubuf))
            }
        }
    }
    /**
     * 检查张数
     * */
    private fun checkNumber(){
        var num =0
        for(data in mViewModel.listPicture){
            if(null!=data.photo){
                num++
            }
        }
        mViewModel.maxPhoneLength.set(num.toString())
    }

    /**
     * 图片点击
     * */
    private fun clickImage(it: Int){
        when(it){
            //选中图片
            1-> choosePicture()
            //隐藏view
//            2-> publishType(TYPE_GRAPHIC,false)
            //查看大图
            3-> ""
            4-> checkNumber()
        }
    }

    /**
     *图片选择
     * */
    private fun choosePicture(){
        var listEasyPhotos : ArrayList<Photo> = ArrayList()
        for(photo in mViewModel.listPicture){
            if(!photo.isChoose){
                photo.photo?.let { listEasyPhotos.add(it) }
            }
        }

        //权限申请返回
        EasyPhotos.createAlbum(activity,true,false,
            GlideEngine.getInstance())
            .setCount(3)
            .setFileProviderAuthority("com.dlxk.zs.fileprovider")//参数说明：见下方`FileProvider的配置`
            .setSelectedPhotos(listEasyPhotos)
            .start(object : SelectCallback() {
                override fun onResult(photos: ArrayList<Photo?>?, isOriginal: Boolean) {
                    photos?.let {
                        if(photos.size>0){
                            mViewModel.listPicture.clear()
                            for(photo in photos){
                                mViewModel.listPicture.add(Picture(false,photo))
                            }
                            //选中的集合是否大于3
                            if(mViewModel.listPicture.size<=2){
                                mViewModel.listPicture.add(Picture(true,null))
                            }
                            feedBackAdapter?.notifyDataSetChanged()
                            checkNumber()
                            aboutRegisterViewModel.feedbackUpimg(mViewModel.listPicture)
                        }
                    }
                }

                override fun onCancel() {

                }
            })
    }
}