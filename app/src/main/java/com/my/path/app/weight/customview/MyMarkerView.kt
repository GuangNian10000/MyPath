package com.my.path.app.weight.customview

import android.annotation.SuppressLint
import android.content.Context
import com.github.mikephil.charting.components.MarkerView
import android.widget.TextView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.utils.MPPointF
import com.my.path.R
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.Utils

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ViewConstructor")
class MyMarkerView(context: Context?,val mList:ArrayList<String>, layoutResource: Int) : MarkerView(context, layoutResource) {
    private val tvContent: TextView

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @SuppressLint("SetTextI18n")
    override fun refreshContent(e: Entry, highlight: Highlight) {
        if (e is CandleEntry) {
            tvContent.text = mList[e.x.toInt()]+"："+Utils.formatNumber(e.high, 0, true)
        } else {
            tvContent.text = mList[e.x.toInt()]+"："+ Utils.formatNumber(e.y, 0, true)
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }

    init {
        tvContent = findViewById(R.id.tvContent)
    }
}