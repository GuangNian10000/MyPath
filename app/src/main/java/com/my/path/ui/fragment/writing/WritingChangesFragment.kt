package com.my.path.ui.fragment.writing

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.my.path.R
import com.my.path.app.base.BaseFragment
import com.my.path.app.config.SysConfig
import com.my.path.app.eventViewModel
import com.my.path.app.ext.clickWithDebounce
import com.my.path.app.ext.getLanguage
import com.my.path.app.util.CacheUtil
import com.my.path.app.util.GlideEngine
import com.my.path.app.util.ImageUtil.loadRoundImage
import com.my.path.data.model.bean.Novel
import com.my.path.data.model.bean.Yc
import com.my.path.data.model.bean.YcData
import com.my.path.databinding.FragmentChangesWritingBinding
import com.my.path.ui.dialog.BookTagSelectionDialog
import com.my.path.ui.dialog.BookTypeSelectionDialog
import com.my.path.ui.dialog.WorkOpDialog
import com.my.path.ui.dialog.ZtListDialog
import com.my.path.viewmodel.request.home.RequestWritingViewModel
import com.my.path.viewmodel.state.writing.WritingChangeViewModel
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.callback.SelectCallback
import com.huantansheng.easyphotos.models.album.entity.Photo
import com.lxj.xpopup.XPopup
import me.guangnian.mvvm.base.appContext
import me.guangnian.mvvm.ext.nav
import me.guangnian.mvvm.ext.parseState

/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　: h5页面
 */
class WritingChangesFragment : BaseFragment<WritingChangeViewModel, FragmentChangesWritingBinding>() {

    private val requestWritingViewModel: RequestWritingViewModel by viewModels()

    companion object{
        val CLASSTYPE_LXFL = "CLASSTYPELXFL"
        val CLASSTYPE_XXSJ = "CLASSTYPEXXSJ"
        val CLASSTYPE_SDFG = "CLASSTYPESDFG"
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel

        mDatabind.proxyClick = ProxyClick()

        arguments?.run {
            getParcelable<Novel>("novel")?.let {
                mViewModel.novel.set(it)
             //   requireContext().loadRoundImage(it.obCover,  mDatabind.viewWritingTop.viewBookCover.bookCover, SysConfig.BookRounded)


                mDatabind.viewWritingTop.viewBookCover.bookNameCover.setTextSizeSp(12f)
                mDatabind.viewWritingTop.viewBookCover.tvUserName.setTextSizeSp(8f)
                if(""!=it.cover){
                    requireContext().loadRoundImage(it.obCover,  mDatabind.viewWritingTop.viewBookCover.bookCover, SysConfig.BookRounded)
                    mDatabind.viewWritingTop.viewBookCover.tvUserName.post {
                        val bool =  mDatabind.viewWritingTop.viewBookCover.tvUserName.canFillFull( "")
                        if(bool){
                            mDatabind.viewWritingTop.viewBookCover.tvUserName.setText("")
                        }
                    }
                    mDatabind.viewWritingTop.viewBookCover.bookNameCover.post {
                        val bool =  mDatabind.viewWritingTop.viewBookCover.bookNameCover.canFillFull( "")
                        if(bool){
                            mDatabind.viewWritingTop.viewBookCover.bookNameCover.setText("")
                        }
                    }
                }else{
                    mDatabind.viewWritingTop.viewBookCover.tvUserName.post {
                        val text = CacheUtil.getUserData()?.author+" 著"
                        val bool = mDatabind.viewWritingTop.viewBookCover.tvUserName.canFillFull(text)
                        if(bool){
                            mDatabind.viewWritingTop.viewBookCover.tvUserName.setText(text)
                        }
                    }
                    mDatabind.viewWritingTop.viewBookCover.bookNameCover.post {
                        val bool = mDatabind.viewWritingTop.viewBookCover.bookNameCover.canFillFull(it.name)
                        if(bool){
                            mDatabind.viewWritingTop.viewBookCover.bookNameCover.setText(it.name)
                        }
                    }
                    requireContext().loadRoundImage(R.drawable.ic_morenstudawesd, mDatabind.viewWritingTop.viewBookCover.bookCover,
                        SysConfig.BookRounded)
                }
            }
        }

        addLoadingObserve(requestWritingViewModel)

        mDatabind.viewWritingTop.bookName.isFocusable = false

        mDatabind.viewWritingTop.bookName.isEnabled = false

        mDatabind.viewWritingBottomAdd.conLx.clickWithDebounce {
            ProxyClick().showLxDialog()
        }

        mDatabind.viewWritingBottomAdd.conXx.clickWithDebounce {
            ProxyClick().showXxDialog()
        }

        mDatabind.viewWritingBottomAdd.conSd.clickWithDebounce {
            ProxyClick().showSdDialog()
        }

        mDatabind.viewWritingBottomAdd.conzpbq.clickWithDebounce {
            ProxyClick().showBookTag()
        }

        mDatabind.viewWritingBottomAdd.conzts.clickWithDebounce {
            ProxyClick().showZtDialog()
        }

        mDatabind.viewWritingBottomAdd.contjy.clickWithDebounce {
            ProxyClick().showTjDialog()
        }

        mDatabind.viewWritingTop.frameLayout2.clickWithDebounce {
            ProxyClick().showPic()
        }
    }

    inner class ProxyClick {
        //创建
        fun createBook(){
            mViewModel.novel.get()?.apply {
                requestWritingViewModel.authorEditBook(bid,obIntro,obRemark,yc,lx,xx,sj,sd,fg,obZtId.toString(),tagid)
            }
        }

        //类型
        fun showLxDialog(){
            requestWritingViewModel.authorYclx()
        }

        fun showXxDialog(){
            requestWritingViewModel.authorXxsj()
        }

        fun showSdDialog(){
            requestWritingViewModel.authorSdfg()
        }

        fun showBookTag(){
            requestWritingViewModel.authorTag()
        }

        fun showZtDialog(){
            requestWritingViewModel.authorZt()
        }

        fun showTjDialog(){
            mViewModel.novel.get()?.let {novel->
                XPopup.Builder(context)
                    .asCustom(WorkOpDialog(requireActivity(),novel.obRemark){ content->
                        novel.obRemark = content
                    }).show()}
            }

        //选择封面
        fun showPic(){
            EasyPhotos.createAlbum(activity,true,false,
                GlideEngine.getInstance())
                .setCount(1)
                .setFileProviderAuthority("com.dlxk.zs.fileprovider")//参数说明：见下方`FileProvider的配置`
                .start(object : SelectCallback() {
                    override fun onResult(photos: ArrayList<Photo?>?, isOriginal: Boolean) {
                        photos?.let {
                            cropImage.launch(
                                options(uri = photos[0]?.uri) {
                                    setCropShape(CropImageView.CropShape.RECTANGLE)
                                    setAspectRatio(600,800)
                                    setCropMenuCropButtonTitle(getString(R.string.shangchuasndwda))
                                }
                            )
                        }
                    }

                    override fun onCancel() {
                    }
                })
        }
    }

    override fun createObserver() {
        //封面
        requestWritingViewModel.authorCoverResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                    data->
                mViewModel.novel.get()?.let {
                    mDatabind.viewWritingTop.viewBookCover.bookNameCover.visibility = View.GONE
                    mDatabind.viewWritingTop.viewBookCover.tvUserName.visibility = View.GONE
                    it.obCover = data.url
                    requireContext().loadRoundImage(it.obCover,  mDatabind.viewWritingTop.viewBookCover.bookCover, SysConfig.BookRounded)
                }
            })
        }

        //连载
        requestWritingViewModel.authorZtResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                    data->
                XPopup.Builder(context)
                    .asCustom(ZtListDialog(requireActivity(),data){tag->
                        mViewModel.novel.get()?.let {
                            it.obZt = tag.name
                            it.obZtId = tag.id
                        }
                    }).show()})
        }

        //标签
        requestWritingViewModel.authorTagResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                    data->
                mViewModel.novel.get()?.apply {
                    if(tagid.isNotEmpty()){
                        val list = tagid.split(",")
                        list.forEach {select->
                            if(""!=select){
                                data.forEach { tag->
                                    if(tag.id==select.toInt()){
                                        tag.selected = true
                                    }
                                }
                            }
                        }
                    }

                    XPopup.Builder(context)
                        .asCustom(BookTagSelectionDialog(requireActivity(),data){tagId,tagText->
                            mViewModel.novel.get()?.let {
                                it.tagid = tagId
                                it.obTag = tagText
                            }
                        })
                        .show()
                }
            })
        }

        //创建作品
        requestWritingViewModel.authorEditBookResult.observe(
            viewLifecycleOwner
        ) { resultState ->
            parseState(resultState, {
                    data->
                nav().navigateUp()
                eventViewModel.writingEvent.value = true
            }, {})
        }

        requestWritingViewModel.authorYclxResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, {
                showTypeDialog(CLASSTYPE_LXFL,it)
            })
        }

        requestWritingViewModel.authorXxsjResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, {
                showTypeDialog(CLASSTYPE_XXSJ,it)
            })
        }

        requestWritingViewModel.authorSdfgResult.observe(viewLifecycleOwner) { resultState ->
            parseState(resultState, {
                showTypeDialog(CLASSTYPE_SDFG,it)
            })
        }
    }

    //弹窗
    private fun showTypeDialog(type:String,list:ArrayList<Yc>){
        mViewModel.novel.get()?.apply {
            when(type){
                CLASSTYPE_LXFL->{
                    list.forEach {
                        if(it.id==yc){
                            it.selected=true

                            it.data.forEach { ycData->
                                if(ycData.id==lx){
                                    ycData.selected=true
                                }
                            }
                        }
                    }
                }
                CLASSTYPE_XXSJ->{
                    list.forEach {
                        if(it.id==xx){
                            it.selected=true

                            it.data.forEach { ycData->
                                if(ycData.id==sj){
                                    ycData.selected=true
                                }
                            }
                        }
                    }
                }
                CLASSTYPE_SDFG->{
                    list.forEach {
                        if(it.id==sd){
                            it.selected=true

                            it.data.forEach { ycData->
                                if(ycData.id==fg){
                                    ycData.selected=true
                                }
                            }
                        }
                    }
                }
            }
            XPopup.Builder(appContext)
                .moveUpToKeyboard(false)
                .asCustom(BookTypeSelectionDialog(requireActivity(),type,list){
                        yc, ycData ->
                    setTypeData(type,yc,ycData)
                })
                .show()
        }
    }

    //设置数据
    private fun setTypeData(type:String,ycs:Yc,ycData: YcData){
        mViewModel.novel.get()?.apply {
            when(type){
                CLASSTYPE_LXFL->{
                    yc = ycs.id
                    lx = ycData.id
                    obYclx = ( ycs.name+"-"+ycData.name)
                }
                CLASSTYPE_XXSJ->{
                    xx = ycs.id
                    sj = ycData.id
                    obXXsj = (ycs.name+"-"+ycData.name)
                }
                CLASSTYPE_SDFG->{
                    sd = ycs.id
                    fg = ycData.id
                    obSdfg = (ycs.name+"-"+ycData.name)
                }
            }
        }
    }


    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            val uriContent = result.uriContent
            val uriFilePath = activity?.let {
                val resultFilePath = result.getUriFilePath(it)
                //上传头像
                if (resultFilePath != null) {
                    mViewModel.novel.get()?.let {novel->
                        context?.let {context->
                            requestWritingViewModel.authorCover(resultFilePath,novel.bid,if(context.getLanguage()) "cn" else "tw")
                        }
                    }

                }
            } // optional usage
        } else {
            // an error occurred
            val exception = result.error
        }
    }
}