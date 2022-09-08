package com.my.path.ui.fragment.chapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.my.path.data.model.bean.Picture
import com.my.path.databinding.ItemAddPictureBinding
import com.my.path.databinding.ItemPictureBinding
import com.bumptech.glide.Glide
import com.my.path.app.ext.clickWithDebounce
import com.my.path.data.model.bean.IntItemOnClick

/**
@author GuangNian
@description:
@date : 4/23/21 3:35 PM
 */
class FeedbackListAdapter(val activity:Activity, val list: MutableList<Picture>, val itemOnClick: IntItemOnClick):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        if(list.size==0){
            list.add(Picture(true,null))
        }
    }

    enum class Item_Type {
        Type_0, Type_1
    }
    /**
     * 空白
     * */
    inner class ViewHolderHe(binding: ItemAddPictureBinding) : RecyclerView.ViewHolder(binding.root) {
    }
    inner class ViewHolderI(binding: ItemPictureBinding) : RecyclerView.ViewHolder(binding.root) {
        val image:ImageView = binding.image
        val imageShanchu:ImageView = binding.imageShanchu
        val spinKit : ProgressBar = binding.spinKit
        val image_cuowu : RelativeLayout = binding.imageCuowu
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemAddPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        if (viewType === Item_Type.Type_0.ordinal) {
            val binding = ItemAddPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolderHe(binding)
        } else if (viewType === Item_Type.Type_1.ordinal) {
            val binding = ItemPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolderI(binding)
        }
        return ViewHolderHe(binding)
    }

    override fun getItemCount(): Int =list.size


    override fun onBindViewHolder(holder:RecyclerView.ViewHolder, position: Int) {
        val dataDTO = list[position]
        /**
         * 空白
         * */
        if (holder is ViewHolderHe) {
            holder.itemView.clickWithDebounce {
               //选择相册
                itemOnClick.onClick(1)
            }
        } else if (holder is ViewHolderI) {

            dataDTO.view = holder.spinKit

            Glide.with(activity).load(dataDTO.photo?.uri).into(holder.image)

            if(null==dataDTO.serviceUrl||""==dataDTO.serviceUrl){
                holder.image_cuowu.visibility = View.VISIBLE
               //holder.spinKit.visibility = View.GONE
            }else{
                holder.image_cuowu.visibility = View.GONE
               // holder.spinKit.visibility = View.VISIBLE
            }

            holder.imageShanchu.clickWithDebounce {
                var a = false
                for(picture in list){
                    if(picture.photo==null){
                        a = true
                    }
                }
                if(list.size==3&&!a){
                    list.add(Picture(true,null))
                }
                //删除该图片
                list.remove(dataDTO)
                notifyDataSetChanged()
                if(list.size==1){
                    //如果删除了所有图片通知隐藏view
                    itemOnClick.onClick(2)
                }
                itemOnClick.onClick(4)
            }

            holder.itemView.clickWithDebounce {
                //查看大图
                itemOnClick.onClick(3)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val dataDTO = list[position]
        return if (dataDTO.isChoose) {
            Item_Type.Type_0.ordinal;
        } else {
            Item_Type.Type_1.ordinal;
        }
        return super.getItemViewType(position)
    }

}