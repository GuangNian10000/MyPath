package com.my.path.app.ext

import android.os.SystemClock
import android.view.View

/**
@author GuangNian
@description:
@date : 2021/11/26 12:21 下午
 */
fun View.clickWithDebounce(debounceTime: Long = 500L, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action()

            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}