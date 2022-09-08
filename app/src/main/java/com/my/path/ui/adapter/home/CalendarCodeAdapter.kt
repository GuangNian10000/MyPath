package com.my.path.ui.adapter.home

import com.my.path.R
import com.my.path.data.model.bean.MeUpdate
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder


class CalendarCodeAdapter(data: ArrayList<MeUpdate>) :
    BaseQuickAdapter<MeUpdate, BaseViewHolder>(
        R.layout.item_calendar, data
    ) {

    override fun convert(helper: BaseViewHolder, item: MeUpdate) {
        //赋值
        item.run {
            //是否为空
            if(!isNull){
                helper.setText(R.id.tvDay,day.toString())
                helper.setText(R.id.tvCodeNum,codeNum.toString())

                if(1==highlight){
                    helper.setTextColor(R.id.tvCodeNum,context.getColor(R.color.text_cad))
                }else{
                    helper.setTextColor(R.id.tvCodeNum,context.getColor(R.color.text_noasd))
                }
            }else{
                helper.setText(R.id.tvDay,"")
                helper.setText(R.id.tvCodeNum,"")
            }

            if(isDay){
                helper.setTextColor(R.id.tvDay,context.getColor(R.color.white))
                helper.setGone(R.id.dian,false)
            }else{
                helper.setTextColor(R.id.tvDay,context.getColor(R.color.text_color_black_22))
                helper.setGone(R.id.dian,true)
            }
        }
    }
}


