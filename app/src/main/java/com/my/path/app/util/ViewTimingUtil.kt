package com.my.path.app.util

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.CountDownTimer
import android.text.Html
import android.view.View
import android.widget.TextView

/**
@author GuangNian
@description: 验证码倒计时
@date : 4/14/21 5:53 PM
 */
class ViewTimingUtil private constructor(){
    companion object{
        @SuppressLint("StaticFieldLeak")
        private var viewTimingUtil: ViewTimingUtil?=null
            get(){
                if (field == null) {
                    field = ViewTimingUtil()
                }
                return field
            }
        @Synchronized
        fun get(): ViewTimingUtil {
            return viewTimingUtil!!
        }

        var isVoice = false
    }

    var view: TextView? = null
    var view2: TextView? = null
    val timer: CountDownTimer = object : CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            if (millisUntilFinished / 1000 != 0L) {
                if(isVoice){
                    view?.setText(Html.fromHtml("收不到短信？试试<font color=\"#FF6464\">语音验证码" +(millisUntilFinished / 1000).toString()+"s<font/>"))
                }else{
                    view?.setText("再次发送("+(millisUntilFinished / 1000).toString() + "s)")
                    view?.setTextColor(Color.parseColor("#CCCCCC"))
                }
            }
            view?.setEnabled(false)
            view2?.visibility = View.GONE
        }

        override fun onFinish() {

            view?.setEnabled(true)
            if(isVoice){
                view?.setText(Html.fromHtml("收不到短信？试试<font color=\"#FF6464\">语音验证码<font/>"))
            }else{
                view?.setText("重新获取")
            }

            view2?.visibility = View.VISIBLE
            isVoice = false
        }
    }

    fun getTimer(view:TextView): CountDownTimer {
        this.view=view
        return timer
    }

    fun getTimer2(view:TextView,view2:TextView): CountDownTimer {
        this.view=view
        this.view2=view2
        return timer
    }

    fun getTimerVoice(view:TextView,view2:TextView): CountDownTimer {
        isVoice = true
        this.view=view
        this.view2=view2
        return timer
    }

    fun getTimerVoiceNull(view:TextView): CountDownTimer {
        isVoice = true
        this.view=view
        return timer
    }

}