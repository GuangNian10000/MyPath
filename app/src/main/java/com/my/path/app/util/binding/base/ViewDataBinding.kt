package com.my.path.app.util.binding.base

import android.os.SystemClock
import android.text.Html
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.databinding.*

import com.my.path.R
import com.my.path.app.config.SysConfig
import com.my.path.app.config.SysConfig.BookRounded
import com.my.path.app.ext.*
import com.my.path.app.util.ImageUtil.loadGardenImage
import com.my.path.app.util.ImageUtil.loadPicImage
import com.my.path.app.util.ImageUtil.loadRoundImage
import me.guangnian.mvvm.ext.nav
import me.guangnian.mvvm.ext.view.textString


/**
 * @author GuangNian
 * @description:
 * @date : 2022/8/1 4:00 下午
 */
object ViewDataBinding {

    @BindingAdapter(value = ["onClickBack"])
    @JvmStatic
    fun onClickBack(view:View,back: Boolean){
        view.clickWithDebounce {
            nav(view).navigateUp()
        }
    }

    @BindingAdapter(value = ["onClickAppUser"])
    @JvmStatic
    fun onClickAppUser(view:View,uid: Int){
        view.clickWithDebounce {
            inAppUser(uid)
        }
    }

    @BindingAdapter(value = ["onClickAppRed"])
    @JvmStatic
    fun onClickAppRed(view:View,rid: Int){
        view.clickWithDebounce {
            inAppRed(rid)
        }
    }

    @BindingAdapter("gone")
    @JvmStatic
    fun setVisibleOrGone(v: View, isVisible: Boolean) {
        v.visibility = if (isVisible) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    @BindingAdapter("visible")
    @JvmStatic
    fun setVisible(v: View, isVisible: Boolean) {
        v.visibility = if (!isVisible) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    @BindingAdapter(value = ["textHtml"])
    @JvmStatic
    fun textHtml(view:TextView,text: String){
        view.text = Html.fromHtml(text)
    }

    @BindingAdapter(value = ["loadHeadImage"])
    @JvmStatic
    fun loadRoundImage(view:ImageView,url: String?){
        val content = view.context
        url?.let {
            content.loadGardenImage(url,view)
        }
    }

    @BindingAdapter(value = ["loadImage"])
    @JvmStatic
    fun loadImage(view:ImageView,url: String?){
        val content = view.context
        url?.let {
            if(it==""){
                content.loadRoundImage(R.drawable.ic_morenstudawesd, view,
                    SysConfig.BookRounded
                )
            }else{
                content.loadRoundImage(url,view, BookRounded)
            }
        }
    }

    @BindingAdapter(value = ["loadPic"])
    @JvmStatic
    fun loadPic(view:ImageView,url: String){
        val content = view.context
        content.loadPicImage(url,view)
    }

    @BindingAdapter(value = ["viewNavigateUp"])
    @JvmStatic
    fun viewNavigateUp(view:View,boolean: String){
        nav(view).navigateUp()
    }

    @BindingAdapter(value = ["isAccording"])
    @JvmStatic
    fun isAccording(view: View, boolean: Boolean) {
        if (boolean) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    @BindingAdapter(value = ["showPwd"])
    @JvmStatic
    fun showPwd(view: EditText, boolean: Boolean) {
        if (boolean) {
            view.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            view.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        view.setSelection(view.textString().length)
    }

    @BindingAdapter(value = ["showClassType"])
    @JvmStatic
    fun showClassType(view: TextView, boolean: Boolean) {
        view.background = if(boolean) getBackgroundExt(R.drawable.bg_class_type_on) else
            getBackgroundExt(R.drawable.bg_class_type_off)
    }

    @BindingAdapter(value = ["showClassTextType"])
    @JvmStatic
    fun showClassTextType(view: TextView, boolean: Boolean) {
        view.setTextColor(if(boolean) getColor(R.color.white) else
            getColor(R.color.shanshansha))
    }

    @BindingAdapter(value = ["showHeadClassType"])
    @JvmStatic
    fun showHeadClassType(view: TextView, boolean: Boolean) {
        view.setTextColor(if(boolean) getColor(R.color.shanshansha) else
            getColor(R.color.msauydae))
    }

    @BindingAdapter(value = ["showPwd"])
    @JvmStatic
    fun showButton(view: TextView, boolean: Boolean) {
        view.background = if(boolean) getBackgroundExt(R.drawable.bg_login_bt_on) else
                getBackgroundExt(R.drawable.bg_login_bt_off)
    }

    @BindingAdapter(value = ["checkChange"])
    @JvmStatic
    fun checkChange(checkbox: CheckBox, listener: CompoundButton.OnCheckedChangeListener) {
        checkbox.setOnCheckedChangeListener(listener)
    }

    @JvmStatic
    @BindingAdapter("onClickServiceAgreement")
    fun bindOnServiceAgreement(view:View,type: String){
        view.clickWithDebounce {
            view.inWebFragment(type)
        }
    }

    @BindingAdapter(value = ["noRepeatClick"])
    @JvmStatic
    fun setOnClick(view: View, clickListener: () -> Unit) {
        val mHits = LongArray(2)
        view.setOnClickListener {
            System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
            mHits[mHits.size - 1] = SystemClock.uptimeMillis()
            if (mHits[0] < SystemClock.uptimeMillis() - 500) {
                clickListener.invoke()
            }
        }
    }

}