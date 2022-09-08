package com.my.path.app.weight.textview



import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.EditText
import androidx.annotation.Nullable

/**
 * @author GuangNian
 * @description:
 * @date : 2021/8/31 5:05 下午
 */
@SuppressLint("AppCompatCustomView")
class NoClickEditText : EditText {

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, @Nullable attrs: AttributeSet?) : this(context, attrs, 0) {}
    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return isEnabled && super.onTouchEvent(event)
    }
}