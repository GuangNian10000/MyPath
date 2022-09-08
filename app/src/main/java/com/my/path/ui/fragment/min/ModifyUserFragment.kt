package com.my.path.ui.fragment.min

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.my.path.R
import com.my.path.app.util.*
import com.my.path.databinding.FragmentModifyUserBinding
import com.my.path.viewmodel.request.ModifyRegisterViewModel
import com.my.path.viewmodel.state.ModifyUserViewModel
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.my.path.app.base.BaseFragment
import com.my.path.app.ext.initTitle
import com.my.path.app.ext.showInputPopup
import com.my.path.app.util.ImageUtil.loadGardenImage
import com.my.path.data.model.bean.PopupCallback
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.callback.SelectCallback
import com.huantansheng.easyphotos.models.album.entity.Photo
import me.guangnian.mvvm.ext.nav
import me.guangnian.mvvm.ext.parseState


/**
 * @author　: GuangNian
 * @date　: 2019/12/23
 * @description　: 修改资料
 */
class ModifyUserFragment : BaseFragment<ModifyUserViewModel, FragmentModifyUserBinding>() {

    private val modifyRegisterViewModel: ModifyRegisterViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        addLoadingObserve(modifyRegisterViewModel)
        mDatabind.viewmodel = mViewModel
        mDatabind.click = ProxyClick()

        mDatabind.include.toolbar.initTitle(context?.getString(R.string.xiugaiziliaodasdx)) {
            nav().navigateUp()
        }
       // initTitle(context?.getString(R.string.xiugaiziliaodasdx))
        modifyRegisterViewModel.meGetInFo()
    }

    override fun createObserver() {
        modifyRegisterViewModel.meGetInFoResult.observe(viewLifecycleOwner, Observer { resultState ->
            parseState(resultState, {
                if(it.st==200){
                    mViewModel.meGetInFo = it
                    mViewModel.penName.set(it.author)
                    mViewModel.userName.set(it.nick)
                    mViewModel.sex.set(if(it.sex==0) getString(R.string.baomis) else if(it.sex==1) getString(R.string.nan ) else getString(R.string.nvs ))
                    mViewModel.userId.set(it.uid.toString())

                    requireContext().loadGardenImage(it.avatar,mDatabind.imageTou)

                    if("0"!=it.author){
                        mDatabind.imageYou213.setImageDrawable(null)
                    }
                }
            },{
            })
        })

        modifyRegisterViewModel.meSaveinfoResult.observe(viewLifecycleOwner, Observer { resultState ->
            parseState(resultState, {
                if(it.st==200){
                   modifyRegisterViewModel.meGetInFo()
                }
            },{
            })
        })

        modifyRegisterViewModel.meUploadResult.observe(viewLifecycleOwner, Observer { resultState ->
            parseState(resultState, {
                if(it.st==200){
                    modifyRegisterViewModel.meGetInFo()
                }
            },{
            })
        })
    }

    inner class ProxyClick {
        //昵称
        fun nick(){
            showInputPopup(getString(R.string.xiugainicasd),mViewModel.userName.get(),7,object:
                PopupCallback {
                override fun callback(str: String) {
                    mViewModel.meGetInFo?.let {
                        it.nick = str
                        modifyRegisterViewModel.meSaveinfo(it)
                    }
                }
            })
        }

        //笔名
        fun penName(){
            if("0"==mViewModel.penName.get()){
                showInputPopup(getString(R.string.ibimingasd),mViewModel.penName.get(),7,object: PopupCallback{
                    override fun callback(str: String) {
                        mViewModel.meGetInFo?.let {
                            modifyRegisterViewModel.meSaveauthor(str)
                        }
                    }
                })
            }
        }

        //头像
        fun userHead(){
            EasyPhotos.createAlbum(activity,true,false,
                GlideEngine.getInstance())
                .setCount(1)
                .setFileProviderAuthority("com.android.zs.fileprovider")//参数说明：见下方`FileProvider的配置`
                .start(object : SelectCallback() {
                    override fun onResult(photos: ArrayList<Photo?>?, isOriginal: Boolean) {
                        photos?.let {
                            cropImage.launch(
                                options(uri = photos[0]?.uri) {
                                    setCropShape(CropImageView.CropShape.RECTANGLE)
                                    setAspectRatio(1,1)
                                    setCropMenuCropButtonTitle(getString(R.string.shangchuasndwda))
                                }
                            )
                        }
                    }

                    override fun onCancel() {
                    }
                })
        }

        //性别
        fun sex(){
            showPickerView()
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
                    modifyRegisterViewModel.meUploadAvatarfile(resultFilePath)
                }
            } // optional usage

        } else {
            // an error occurred
            val exception = result.error
        }
    }

    /**
     * 选择性别
     * */
    private fun showPickerView() {
        val listData: MutableList<String> = java.util.ArrayList()
        listData.add(getString(R.string.baomis))
        listData.add(getString(R.string.nan))
        listData.add(getString(R.string.nvs))

        val pvOptions: OptionsPickerView<*> = OptionsPickerBuilder(
            activity
        ) { options1, _, _, _ ->
            mViewModel.meGetInFo?.let {
                it.sex =options1
                modifyRegisterViewModel.meSaveinfo(it)
            }
        }
            .setSelectOptions(0)
            .setOutSideCancelable(true)
            .setLineSpacingMultiplier(3f)

            .build<Any>()
        pvOptions.setPicker(listData as List<Nothing>)
        pvOptions.show()
    }

}