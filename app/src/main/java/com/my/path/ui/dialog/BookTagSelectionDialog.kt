package com.my.path.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.my.path.R
import com.my.path.app.ext.init
import com.my.path.app.ext.makeToast
import com.my.path.data.model.bean.Tag
import com.my.path.databinding.PopupWindowBookTagSelectBinding
import com.drake.brv.utils.*
import com.lxj.xpopup.core.BottomPopupView
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter


class BookTagSelectionDialog(private val mActivity: Activity,
                             private val mListData:ArrayList<Tag>,
                             private val onCallback :((String,String) -> Unit)) : BottomPopupView(mActivity){

    private var binding: PopupWindowBookTagSelectBinding?=null

    private val mListYcData:ArrayList<Tag> = ArrayList()

    override fun getImplLayoutId():Int = R.layout.popup_window__book_tag_select

    override fun onCreate() {
        super.onCreate()

        binding = DataBindingUtil.bind(popupImplView)

        initData()

        initOnClick()

        initFlowlayout()//更新已选列表
    }

    @SuppressLint("CutPasteId", "NotifyDataSetChanged")
    private fun initData(){
        binding?.apply {

            include3.title.text = context.getString(R.string.zuoppinjgdase)

            recyclerView.linear().setup {
                recyclerView.init(GridLayoutManager(context,3))

                addType<Tag>(R.layout.item_writing_class_tag)

                R.id.itemFrame.onClick {
                    if(mListYcData.size==4){
                        makeToast(context.getString(R.string.zuiduozhinengxunasdas))
                        return@onClick
                    }
                    mListData[modelPosition].selected =  !mListData[modelPosition].selected //选中

                    notifyItemChanged(modelPosition)

                    initFlowlayout()//更新已选列表
                }
            }.models=mListData
        }
    }

    private fun initFlowlayout(){
        binding?.apply {
            mListYcData.clear()
            mListData.forEach { yc ->
                if(yc.selected){
                    mListYcData.add(yc)
                }
            }

            if(mListYcData.size>0){
                tvxuasd.visibility = View.GONE
            }else{
                tvxuasd.visibility = View.VISIBLE
            }

            flowlayoutCom.adapter = object : TagAdapter<Tag?>(mListYcData as List<Tag?>?) {
                @SuppressLint("NotifyDataSetChanged")
                override fun getView(parent: FlowLayout?, position: Int, t: Tag?): View {
                    val tabItem =
                        View.inflate(mActivity, R.layout.item_writing_class_type_select, null)
                    val textView = tabItem.findViewById<TextView>(R.id.textView2)
                    val view = tabItem.findViewById<View>(R.id.view)
                    textView.text = t?.name

                    view.setOnClickListener {
                        //删除标签
                        mListData.forEach {tag->
                            if(tag.id == mListYcData[position].id){
                                tag.selected = false
                            }
                        }
                        initFlowlayout()
                        recyclerView.bindingAdapter.notifyDataSetChanged()
                    }
                    return tabItem
                }
            }
            flowlayoutCom.adapter.notifyDataChanged()
        }
    }

    private fun initOnClick(){
        binding?.apply {
            include3.imageView4.setOnClickListener {
                dismiss()
            }
            include3.tvOk.setOnClickListener {
                if(mListYcData.size>0){
                    var tagId = ""
                    var tagText = ""
                    mListYcData.forEach {
                        tagId+=it.id.toString()+","
                        tagText+=it.name+","
                    }

                    val t  = tagId.substring(0,tagId.length-1)
                    val x  = tagText.substring(0,tagText.length-1)
                    onCallback.invoke(t,x)
                    dismiss()
                }else{
                    onCallback.invoke("","")
                    dismiss()
                   // makeToast(context.getString(R.string.zhishaoxuanzeyidiasndae))
                }
            }
        }
    }

}
