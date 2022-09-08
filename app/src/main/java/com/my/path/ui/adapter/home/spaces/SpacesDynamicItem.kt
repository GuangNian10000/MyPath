package com.my.path.ui.adapter.spaces

import android.app.Activity
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author GuangNian
 * @description:
 * @date : 2021/6/4 10:50 上午
 */
class SpacesDynamicItem(private val activity: Activity) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        // outRect.top = dpToPx(10f);
        if (parent.getChildAdapterPosition(view) % 3 == 0) {
            outRect.left = dpToPx(5f)
            outRect.right = dpToPx(5f)
        } else if (parent.getChildAdapterPosition(view) % 3 == 1) {
            outRect.left = dpToPx(5f)
            outRect.right = dpToPx(5f)
        } else if (parent.getChildAdapterPosition(view) % 3 == 2) {
            outRect.left = dpToPx(5f)
            outRect.right = dpToPx(5f)
        }
    }

    fun dpToPx(valueInD: Float?): Int {
        val metrics = activity.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInD!!, metrics).toInt()
    }
}