package com.my.path.app.weight.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.widget.TextView
import com.my.path.R
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class QuickLocationBar : View {
    private val characters = arrayOf(
        "#", "A", "B", "C", "D", "E", "F", "G",
        "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
        "U", "V", "W", "X", "Y", "Z"
    )
    private var choose = -1
    private val paint = Paint()
    private var mOnTouchLetterChangedListener: OnTouchLetterChangedListener? = null
    private var mTextDialog: TextView? = null

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {        // TODO Auto-generated constructor stub
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {        // TODO Auto-generated constructor stub
    }

    constructor(context: Context?) : super(context) {        // TODO Auto-generated constructor stub
    }

    fun setOnTouchLitterChangedListener(
        onTouchLetterChangedListener: OnTouchLetterChangedListener?
    ) {
        mOnTouchLetterChangedListener = onTouchLetterChangedListener
    }

    fun setTextDialog(dialog: TextView?) {
        mTextDialog = dialog
    }

    override fun onDraw(canvas: Canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas)
        val width = width
        val height = height
        val singleHeight = height / characters.size
        for (i in characters.indices) {
            // 对paint进行相关的参数设置
            paint.color = resources.getColor(R.color.text_color_black_21)
            paint.typeface = Typeface.DEFAULT_BOLD
            paint.isAntiAlias = true
            paint.textSize = 150 * width.toFloat() / 320
            if (i == choose) { // choose变量表示当前显示的字符位置，若没有触摸则为-1
                paint.color = resources.getColor(R.color.text_color_black_22)
                paint.isFakeBoldText = true
            }
            // 计算字符的绘制的位置
            val xPos = width / 2 - paint.measureText(characters[i]) / 2
            val yPos = (singleHeight * i + singleHeight).toFloat()
            // 在画布上绘制字符
            canvas.drawText(characters[i], xPos, yPos, paint)
            paint.reset() // 每次绘制完成后不要忘记重制Paint
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        val y = event.y
        val c = (y / height * characters.size).toInt()
        Log.d("adasd", "$y----$height----")
        when (action) {
            MotionEvent.ACTION_UP -> {
                choose = -1 //
                setBackgroundColor(0x0000)
                invalidate()
                if (mTextDialog != null) {
                    mTextDialog!!.visibility = GONE
                }
            }
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE ->                 //setBackgroundColor(getResources().getColor(R.color.bg_653fac));
                if (choose != c) {
                    if (c >= 0 && c < characters.size) {
                        if (mOnTouchLetterChangedListener != null) {
                            mOnTouchLetterChangedListener!!
                                .touchLetterChanged(characters[c])
                        }
                        if (mTextDialog != null) {
                            mTextDialog!!.text = characters[c]
                            mTextDialog!!.visibility = VISIBLE
                        }
                        choose = c
                        invalidate()
                    }
                }
        }
        return true
    }

    interface OnTouchLetterChangedListener {
        fun touchLetterChanged(s: String?)
    }
}