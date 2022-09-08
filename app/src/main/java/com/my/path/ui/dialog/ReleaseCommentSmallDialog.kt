package com.my.path.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.my.path.R
import com.my.path.app.config.SysConfig
import com.my.path.app.ext.clickWithDebounce
import com.my.path.app.ext.getColor
import com.my.path.app.ext.hideSoftKeyboard
import com.my.path.app.util.GlideEngine
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.callback.SelectCallback
import com.huantansheng.easyphotos.models.album.entity.Photo
import com.lxj.xpopup.core.BottomPopupView



/**
 * 回复评论
 * */
class ReleaseCommentSmallDialog(var mActivity: Activity,val hint:String = "", val back: (content:String,path:String)->Unit) : BottomPopupView(mActivity){

    override fun getImplLayoutId():Int = R.layout.popup_window_comments_sm

    private var picStr  = ""

    override fun onCreate() {
        super.onCreate()
        initData()
        initOnClick()
    }

    @SuppressLint("CutPasteId", "NotifyDataSetChanged")
    private fun initData(){

        findViewById<TextView>(R.id.tv_tijiao).clickWithDebounce {
            back.invoke(findViewById<EditText>(R.id.et_content).text.toString(),picStr)
            dismiss()
        }

        findViewById<ImageView>(R.id.imageColse).clickWithDebounce {
            picStr = ""
            findViewById<ConstraintLayout>(R.id.constraintneir).visibility = View.GONE
            addTextChanged( findViewById<EditText>(R.id.et_content).text.toString())
        }

        findViewById<View>(R.id.viewsa).clickWithDebounce {
            setImage()
        }
        findViewById<EditText>(R.id.et_content).hint = hint
        //监听输入框
        findViewById<EditText>(R.id.et_content).addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                addTextChanged(editable.toString())
            }
        })

        showInput(findViewById(R.id.et_content))

        findViewById<RelativeLayout>(R.id.relativeLayout3).clickWithDebounce {
            showInput(findViewById(R.id.et_content))
        }

    }

    private fun addTextChanged(str:String){
        if (str != ""||picStr!="") {
            findViewById<TextView>(R.id.tv_tijiao).setTextColor(getColor(R.color.base_color))
        } else {
            findViewById<TextView>(R.id.tv_tijiao).setTextColor(getColor(R.color.read_bg_tu_2))
        }
    }

    fun setImage(){
        EasyPhotos.createAlbum(mActivity,true,false,
            GlideEngine.getInstance())
            .setCount(1)
            .setFileProviderAuthority(SysConfig.page)//参数说明：见下方`FileProvider的配置`
            .start(object : SelectCallback() {
                override fun onResult(photos: ArrayList<Photo?>?, isOriginal: Boolean) {
                    val pth = photos?.get(0)?.path
                    pth?.let {
                        picStr = it
                        Glide.with(mActivity).load(it).
                        into(findViewById<ImageView>(R.id.imageView56))
                        findViewById<ConstraintLayout>(R.id.constraintneir).visibility = View.VISIBLE
                        addTextChanged( findViewById<EditText>(R.id.et_content).text.toString())
                    }
                }

                override fun onCancel() {
                }
            })
    }

    private fun initOnClick(){
    }

    /**
     * 显示键盘
     *
     * @param et 输入焦点
     */
    private fun showInput(et: EditText) {
        et.requestFocus()
        val imm = mActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onDismiss() {
        findViewById<EditText>(R.id.et_content)?.apply {
            context.hideSoftKeyboard(this)
        }
        super.onDismiss()
    }
}
