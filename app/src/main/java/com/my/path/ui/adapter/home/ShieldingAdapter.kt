package com.my.path.ui.adapter.home

import com.my.path.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.my.path.app.util.ImageUtil.loadGardenImage
import com.my.path.data.model.bean.Uinfo
import me.guangnian.mvvm.base.appContext


class ShieldingAdapter(data: ArrayList<Uinfo>) :
    BaseQuickAdapter<Uinfo, BaseViewHolder>(
        R.layout.item_book_report, data
    ) {

    override fun convert(helper: BaseViewHolder, item: Uinfo) {
        //赋值
        item.run {
            appContext.loadGardenImage(avatar,helper.getView(R.id.imageUser));
            helper.setText(R.id.tvUserName,nick)
        }
    }
}


