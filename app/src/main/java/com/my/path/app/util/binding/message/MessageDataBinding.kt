package com.my.path.app.util.binding.message

import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.ctetin.expandabletextviewlibrary.ExpandableTextView
import com.my.path.R
import com.my.path.app.ext.*
import com.my.path.app.util.BitmapUtil.clipBitMap
import com.my.path.data.model.bean.Reply
import com.zhy.view.flowlayout.TagFlowLayout.dip2px
import me.guangnian.mvvm.base.appContext
import me.guangnian.mvvm.ext.util.dp2px
import java.util.regex.Pattern


/**
 * @author GuangNian
 * @description:
 * @date : 2022/8/10 11:56 上午
 */
object MessageDataBinding {

    @BindingAdapter(value = ["showImage"])
    @JvmStatic
    fun showImage(view: ImageView, src:String?) {
        src?.let {
            view.clickWithDebounce {
                view.showImg(src)
            }
        }
    }
    @BindingAdapter(value = ["setImageMedal"])
    @JvmStatic
    fun setImageMedal(view: ImageView, lv:Int) {
        if(lv==0){
            view.visibility = View.GONE
        }else{
            view.visibility = View.VISIBLE
        }
        view.setImageDrawable(getBackgroundExt(when(lv){
            1 -> R.drawable.ic_hz_xc
            2 -> R.drawable.ic_hz_xh
            3 -> R.drawable.ic_hz_xhui
            4 -> R.drawable.ic_xm_xm
            5 -> R.drawable.ic_hz_xy
            6 -> R.drawable.ic_hz_xs
            7 -> R.drawable.ic_hz_xinghe
            8 -> R.drawable.ic_hz_xkong
            else-> R.drawable.ic_hz_xc }))
    }

    @BindingAdapter(value = ["setImageVip"])
    @JvmStatic
    fun setImageVip(view: ImageView, lv:Int) {
        when(lv){
            1->{
                view.setImageDrawable(getBackgroundExt(R.drawable.vip_1))
            }
            2->{
                view.setImageDrawable(getBackgroundExt(R.drawable.vip_2))
            }
            3->{
                view.setImageDrawable(getBackgroundExt(R.drawable.vip_3))
            }
            4->{
                view.setImageDrawable(getBackgroundExt(R.drawable.vip_4))
            }
            5->{
                view.setImageDrawable(getBackgroundExt(R.drawable.vip_5))
            }
        }
    }

    @BindingAdapter(value = ["setImageLv"])
    @JvmStatic
    fun setImageLv(view: ImageView, lv:String?) {
        val drawable = getBackgroundExt(R.drawable.bg_lv)
        lv?.let {
            if(lv!=""){
                val REGEX = "[^0-9]"
                val mLvs = Pattern.compile(REGEX).matcher(lv).replaceAll("").trim().toInt()
                drawable?.apply {
                    val bitmapDrawable = this as BitmapDrawable
                    val bit =  bitmapDrawable.bitmap
                    val py = appContext.dp2px(4).toFloat()-0.5f//间距
                    val px = appContext.dp2px(2).toFloat()//间距
                    val w = appContext.dp2px(38).toFloat()
                    val h = appContext.dp2px(16).toFloat()
                    var x = 0f
                    var y = 0f

                    if(mLvs<=10){
                        y = ((h+py)* (mLvs-1))
                    }else if(mLvs<=20){
                        x = (w+px)
                        y = (h+py)* (mLvs-1-10)
                    }else if(mLvs<=30){
                        x = (w+px)* (2)
                        y = (h+py)* (mLvs-1-20)
                    }else if(mLvs<=40){
                        x = (w+px)* (3)
                        y = (h+py)* (mLvs-1-30)
                    }else if(mLvs<=50){
                        x = (w+px)* (4)
                        y = (h+py)* (mLvs-1-40)
                    }

                    if(mLvs==11||mLvs==21||mLvs==31||mLvs==41){
                        y-= appContext.dp2px(1).toFloat()
                    }

                    val textR = clipBitMap(bit, x, y, w, h,0f)
                    view.setImageBitmap(textR)
                }
            }
        }
    }

    @BindingAdapter(value = ["setExpandableTextView"])
    @JvmStatic
    fun setExpandableTextView(view: ExpandableTextView, str:String) {
        view.setContent(str)
    }

    @BindingAdapter(value = ["addContentComment"])
    @JvmStatic
    fun addContentComment(view: LinearLayout, list:ArrayList<Reply>) {
        view.removeAllViews()
        list.forEach {data->
            val textView =TextView(view.context)
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            textView.setLineSpacing(0f,1.5f)
            layoutParams.setMargins(0, 0, 0,dip2px(appContext, 8.0f)) //4个参数按顺序分别是左上右下
            textView.layoutParams = layoutParams
            view.setReplySplice(textView,data.nick,data.uid,data.content,data.img)
            view.addView(textView)
        }
    }

    @BindingAdapter(value = ["showLike"])
    @JvmStatic
    fun showLike(view: ImageView, boolean: Boolean) {
        view.setImageDrawable(if(boolean) getBackgroundExt(R.drawable.ic_dianzan_you) else
            getBackgroundExt(R.drawable.ic_dianzan_wu))
    }

    @BindingAdapter(value = ["showFlowBookBg"])
    @JvmStatic
    fun showFlowBookBg(view: TextView, boolean: Boolean) {
        view.background = if(boolean) getBackgroundExt(R.drawable.bg_class_flow_type_on) else
            getBackgroundExt(R.drawable.bg_class_flow_type_off)
        view.setTextColor(if(boolean) getColor(R.color.base_color) else
            getColor(R.color.balibali))
    }

   @BindingAdapter(value = ["showNum"])
   @JvmStatic
   fun setShowNum(view: TextView, num: String?) {
    val params =  view.layoutParams
    if(null!=num&&""!=num){
     val num = num.toInt()
     if(num<=0){
      view.visibility = View.GONE
     }else if(num<10){
      params.height = appContext.dp2px(16)
      params.width = appContext.dp2px(16)
      view.layoutParams  = params
      view.background = getBackgroundExt(R.drawable.bg_xiaoxiyuandianawe)
     }else{
      params.height = ViewGroup.LayoutParams.WRAP_CONTENT
      params.width = ViewGroup.LayoutParams.WRAP_CONTENT
      view.layoutParams  = params
      view.background = getBackgroundExt(R.drawable.bg_xiaoxiyuandianawe2)
     }
    }

    view.text = num?:""
   }

   /**
    * 当 `userNameAttrChanged` 的 InverseBindingListener onChange()被调用时，此方法会被调用
    */
   @InverseBindingAdapter(attribute = "showNum")
   @JvmStatic
   fun getShowNum(view: TextView): String {
    return view.text.toString()
   }

   /***
    * 这个 Binding Adapter不需要定在xml中，但必须要指定后缀 "AttrChanged"
    * 也就是：  更新属性+AttrChanged
    * 这里是 监听userName 的值
    */
   @BindingAdapter("showNumAttrChanged")
   @JvmStatic
   fun setShowNumListener(view: TextView, listener: InverseBindingListener?) {
    //监听文本输入框
    view.onFocusChangeListener = View.OnFocusChangeListener { focusedView, hasFocus ->
     if (!hasFocus) {
      // If the focus left, update the listener
      listener?.onChange()
     }
    }
   }
}