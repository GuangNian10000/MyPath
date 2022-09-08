package com.my.path.data.model.bean

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler


/**
 * @author GuangNian
 * @description:
 * @date : 2022/3/10 2:39 下午
 */
class LabelPointTextFormatter(var mLabels: ArrayList<Entry> ): IValueFormatter {
    override fun getFormattedValue(
        value: Float,
        entry: Entry?,
        dataSetIndex: Int,
        viewPortHandler: ViewPortHandler?
    ): String = mLabels[entry!!.x.toInt()].y.toInt().toString()
}