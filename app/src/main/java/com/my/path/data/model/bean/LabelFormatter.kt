package com.my.path.data.model.bean

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter


/**
 * @author GuangNian
 * @description:
 * @date : 2022/3/10 2:39 下午
 */
class LabelFormatter(var mLabels: ArrayList<String> ): IAxisValueFormatter {
    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        return try {
            mLabels[value.toInt() % mLabels.size].replace("2022-","")
        } catch (e: Exception) {
            e.printStackTrace()
            if(mLabels.size>0){
                mLabels[0]
            }else{
                ""
            }

        }
    }

    override fun getDecimalDigits(): Int =0
}