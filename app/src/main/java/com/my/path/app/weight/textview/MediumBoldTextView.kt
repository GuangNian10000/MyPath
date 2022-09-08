package com.my.path.app.weight.textview



import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatTextView
import com.my.path.R
import me.guangnian.mvvm.callback.databind.StringObservableField

/**
 * @author GuangNian
 * @description:
 * @date : 2021/8/31 5:05 下午
 */
class MediumBoldTextView : AppCompatTextView {
    var strokeWidth: Float = 0.7F
    var bookId = StringObservableField("")
    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, @Nullable attrs: AttributeSet?) : this(context, attrs, 0) {}
    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.MediumBoldTextView, defStyleAttr, 0)
        arr.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        //获取当前控件的画笔
        val paint = paint
        //设置画笔的描边宽度值
        paint.strokeWidth = strokeWidth
        paint.style = Paint.Style.FILL_AND_STROKE
        super.onDraw(canvas)
    }
}