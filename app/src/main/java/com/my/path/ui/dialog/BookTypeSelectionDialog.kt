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
import com.my.path.data.model.bean.Yc
import com.my.path.data.model.bean.YcData
import com.my.path.databinding.PopupWindowBookTypeSelectBinding
import com.my.path.ui.fragment.writing.WritingAddFragment.Companion.CLASSTYPE_LXFL
import com.my.path.ui.fragment.writing.WritingAddFragment.Companion.CLASSTYPE_SDFG
import com.my.path.ui.fragment.writing.WritingAddFragment.Companion.CLASSTYPE_XXSJ
import com.drake.brv.utils.*
import com.lxj.xpopup.core.BottomPopupView
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter


class BookTypeSelectionDialog(private val mActivity: Activity,
                              private val type:String,
                              private val mListData:ArrayList<Yc>,
                              private val onCallback :((Yc, YcData) -> Unit)) : BottomPopupView(mActivity){

    private var binding: PopupWindowBookTypeSelectBinding?=null

    private var mIndex:Int = 0//右侧数据位置

    private val mListYcData:ArrayList<YcData> = ArrayList()

    override fun getImplLayoutId():Int = R.layout.popup_window__book_type_select

    override fun onCreate() {
        super.onCreate()

        binding = DataBindingUtil.bind(popupImplView)

        initData()

        initOnClick()

        initFlowlayout()//更新已选列表

        initYcData()

    }

    private fun initYcData() {
        val list = mListData.filter {
            it.selected
        }
        if(list.isNotEmpty()){
            val data = list[0]

            val modelPosition = mListData.indexOf(data)

            //选中
            mListData.forEach {
                it.selected = false
            }
            mListData[modelPosition].selected = true
            binding?.recyclerView2?.models = mListData

            //显示右侧数据
            mIndex = modelPosition
            binding?.recyclerView2?.models = mListData[mIndex].data
        }
    }

    @SuppressLint("CutPasteId", "NotifyDataSetChanged")
    private fun initData(){
        binding?.apply {
            when(type){
                CLASSTYPE_LXFL->{
                    include3.title.text = context.getString(R.string.leixifngdjkase)
                }
                CLASSTYPE_XXSJ->{
                    include3.title.text = context.getString(R.string.xingxiangshijieaoase)
                }
                CLASSTYPE_SDFG->{
                    include3.title.text = context.getString(R.string.shidaifenggeaseasd)
                }
            }

            //左边list
            recyclerView1.linear().setup {
                addType<Yc>(R.layout.item_writing_class_type)
                R.id.itemFrame.onClick {
                    //选中
                    mListData.forEach {
                        it.selected = false
                    }
                    mListData[modelPosition].selected = true
                    recyclerView1.models = mListData

                    //显示右侧数据
                    mIndex = modelPosition
                    recyclerView2.models = mListData[mIndex].data
                }
            }.models = mListData

            //右边list
            recyclerView2.linear().setup {
                recyclerView2.init(GridLayoutManager(context,2))
                addType<YcData>(R.layout.item_writing_class_type_child)

                R.id.itemFrame.onClick {
                    mListData.forEach {
                       it.data.forEach { ycData -> //所有二级选项false
                           ycData.selected = false
                       }
                    }
                    mListData[mIndex].data[modelPosition].selected = true //选中

                    recyclerView2.models =  mListData[mIndex].data //刷新

                    initFlowlayout()//更新已选列表
                }
            }.models
        }


    }

    private fun initFlowlayout(){
        binding?.apply {
            mListYcData.clear()
            mListData.forEach { yc ->
                if(yc.selected){
                    yc.data.forEach {ycData ->
                        if(ycData.selected){
                            mListYcData.add(ycData)
                        }
                    }
                }
            }

            flowlayoutCom.adapter = object : TagAdapter<YcData?>(mListYcData as List<YcData?>?) {
                override fun getView(parent: FlowLayout?, position: Int, t: YcData?): View {
                    val tabItem =
                        View.inflate(mActivity, R.layout.item_writing_class_type_select, null)
                    val textView = tabItem.findViewById<TextView>(R.id.textView2)
                    val view = tabItem.findViewById<View>(R.id.view)
                    textView.text = t?.name

                    view.setOnClickListener {
                        //删除标签
                        mListData.forEach {
                            it.data.forEach { ycData -> //所有二级选项false
                                ycData.selected = false
                            }
                        }
                        initFlowlayout()
                        recyclerView1.bindingAdapter.notifyDataSetChanged()
                        recyclerView2.bindingAdapter.notifyDataSetChanged()
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
                    onCallback.invoke(mListData[mIndex],mListYcData[0])
                    dismiss()
                }else{
                    makeToast(context.getString(R.string.zhishaoxuanzeyidiasndae))
                }
            }
        }
    }

}
