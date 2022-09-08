package com.my.path.app.weight.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.Nullable
import com.github.mikephil.charting.charts.BarChart


/**
 * @author GuangNian
 * @description:
 * @date : 2022/3/18 5:03 下午
 */
class ScrollViewBarChart: BarChart {
    var downPoint = PointF()

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, @Nullable attrs: AttributeSet?) : this(context, attrs, 0) {}
    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(evt: MotionEvent): Boolean {
        when (evt.action) {
            MotionEvent.ACTION_DOWN -> {
                downPoint.x = evt.x
                downPoint.y = evt.y
            }
            MotionEvent.ACTION_MOVE -> {
//                if (scaleX > 1 && Math.abs(evt.x - downPoint.x) > 5) {
//                    parent.requestDisallowInterceptTouchEvent(true)
//                }
                parent.requestDisallowInterceptTouchEvent(true)
            }
        }
        return super.onTouchEvent(evt)
    }
}