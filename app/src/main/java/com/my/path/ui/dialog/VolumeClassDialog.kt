package com.my.path.ui.dialog

import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.my.path.R
import com.my.path.app.eventViewModel
import com.my.path.app.ext.clickWithDebounce
import com.my.path.app.ext.makeToast
import com.my.path.data.model.bean.*
import com.drake.brv.utils.*
import com.drake.net.Post
import com.drake.net.utils.scope
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BottomPopupView

class VolumeClassDialog(var mActivity: Fragment,val bid:Int, val mList : ArrayList<Volume>, val back: (Volume)->Unit) : BottomPopupView(mActivity.requireContext()){

    override fun getImplLayoutId():Int = R.layout.popup_window_volume_class

    override fun onCreate() {
        super.onCreate()

        findViewById<TextView>(R.id.imageView22).clickWithDebounce {
            dismiss()
        }

        findViewById<TextView>(R.id.tvxjian).clickWithDebounce {

            XPopup.Builder(mActivity.requireActivity())
                .asCustom(VolumeClassAddDialog(mActivity.requireActivity()){
                    scope {
                        val bean = Post<Addvolume>("/author/addvolume/"){
                            param("bid", bid)
                            param("volumename", it)
                        }.await()
                        if(200==bean.st){
                            eventViewModel.draftBoxEvent.postValue(true)
                            mList.add(Volume(bean.cid,false,it))
                            findViewById<RecyclerView>(R.id.recyclerView).models = mList
                            findViewById<RecyclerView>(R.id.recyclerView).bindingAdapter.notifyDataSetChanged()
                        }else{
                            makeToast(bean.msg)
                        }
                    }

                }).show()
        }

        findViewById<RecyclerView>(R.id.recyclerView).linear().setup {

            addType<Volume>(R.layout.item_writing_class_volume)

            R.id.constraint.onClick {
                getModelOrNull<Volume>()?.let {
                    back.invoke(it)
                }
                dismiss()
            }

            R.id.bianjis.onClick {
                getModelOrNull<Volume>()?.let {
                    XPopup.Builder(mActivity.requireContext())
                        .asCustom(VolumeClassChangeDialog(mActivity,it.name){ text, del->
                            if(del){//删除
                                scope {
                                    val bean = Post<BaseCode>("/author/chapterdel/"){
                                        param("bid", bid)
                                        param("cid", it.cid)
                                    }.await()
                                    if(200==bean.st){
                                        eventViewModel.draftBoxEvent.postValue(true)
                                        makeToast(bean.result)
                                        findViewById<RecyclerView>(R.id.recyclerView).mutable.remove(it) // 先删除数据
                                        notifyItemRemoved(modelPosition)
                                    }else{
                                        makeToast(bean.msg)
                                    }
                                }
                            }else{//修改
                                scope {
                                    val bean = Post<BaseCode>("/author/editchapterename/"){
                                        param("bid", bid)
                                        param("cid", it.cid)
                                        param("chaptername", text)
                                    }.await()
                                    if(200==bean.st){
                                        eventViewModel.draftBoxEvent.postValue(true)
                                        it.name = text
                                        notifyItemChanged(modelPosition)
                                    }else{
                                        makeToast(bean.msg)
                                    }
                                }
                            }
                        }).show()
                }

            }

        }.models=mList

    }
}
