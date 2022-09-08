package com.my.path.ui.dialog

import android.app.Activity
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.my.path.R
import com.my.path.data.model.bean.Book
import com.my.path.data.model.bean.MeGetBook
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.drake.net.Post
import com.drake.net.utils.scope
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.lxj.xpopup.impl.PartShadowPopupView


class BookTagDialog(mActivity: Activity,
                    private val bid:Int,
                    private val onCallback :((Book) -> Unit)) : PartShadowPopupView(mActivity){

    override fun getImplLayoutId():Int = R.layout.popup_window_book_tag

    override fun onCreate() {

        super.onCreate()

        val rv = findViewById<RecyclerView>(R.id.rv)

        rv.linear().setup {
            //初始化热门Recyclerview
            val layoutManager = FlexboxLayoutManager(context)
            //方向 主轴为水平方向，起点在左端
            layoutManager.flexDirection = FlexDirection.ROW
            //左对齐
            layoutManager.justifyContent = JustifyContent.FLEX_START

            rv?.layoutManager = layoutManager

            addType<Book>(R.layout.item_flow_layout_book)

            R.id.flow_tag.onClick {
                rv?.apply {
                    onCallback.invoke(bindingAdapter.getModel(modelPosition))
                }
                dismiss()
            }
        }.models

        scope {
            val bean = Post<MeGetBook>("/me/getbook/").await()
            if(200==bean.st){
                bean.books.forEach {
                    if(it._id== bid){
                        it.isSelect = true
                    }
                }

                rv.models = bean.books
            }
        }
    }
}
