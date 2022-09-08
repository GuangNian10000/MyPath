package com.my.path.app.util.binding.writing

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.my.path.R
import com.my.path.app.ext.getBackgroundExt
import com.my.path.app.ext.getColor
import com.my.path.app.util.ImageUtil.loadGaussianImage
import com.my.path.app.weight.textview.VerticalTextView
import com.my.path.data.model.bean.ChapterContent

/**
 * @author GuangNian
 * @description:
 * @date : 2022/8/10 11:56 上午
 */
object WritingDataBinding {

    @BindingAdapter(value = ["userNameNUll"])
    @JvmStatic
    fun userNameNUll(view: VerticalTextView, name:String?) {
        name?.let {
            view.setTextSizeSp(4f)
            view.post {
                val bool = view.canFillFull( name)
                if(bool){
                    view.setText(name)
                }
            }
        }

    }

    @BindingAdapter(value = ["bookNameNUll"])
    @JvmStatic
    fun bookNameNUll(view: VerticalTextView, name:String?) {
        name?.let {
            view.setTextSizeSp(6f)
            view.post {
                val bool = view.canFillFull( name)
                if(bool){
                    view.setText(name)
                }
            }
        }
    }

    @BindingAdapter(value = ["loadPicImage"])
    @JvmStatic
    fun loadPicImage(view: ImageView, url:Int) {
        view.setImageDrawable(getBackgroundExt(url))
      //  view.context.loadPicImage(url,view)
    }

    @BindingAdapter(value = ["loadGaussianImage"])
    @JvmStatic
    fun loadGaussianImage(view: ImageView, url:String?) {
        url?.let {
            view.context.loadGaussianImage(url,view)
        }
    }

    @BindingAdapter(value = ["setHeightLi"])
    @JvmStatic
    fun setHeightLi(view: TextView, select:Boolean) {
        if(select){
            view.setTextColor(getColor(R.color.base_color))
        }else{
            view.setTextColor(getColor(R.color.shanshansha))
        }
    }

    @BindingAdapter(value = ["setChapterState"])
    @JvmStatic
    fun setChapterState(view: TextView, state:Int) {
        if(state==2){
            view.setText("发布")
        }else{
            view.setText("收回")
        }
    }

    @BindingAdapter(value = ["setVolume"])
    @JvmStatic
    fun setVolume(view: TextView, data: ChapterContent?) {
        data?.let {
            for (volume in data.volume) {
                if(volume.cid.toString()==data.vcid){
                    view.text = volume.name
                    return
                }
            }
        }
    }
}