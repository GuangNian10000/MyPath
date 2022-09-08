package com.my.path.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import kotlinx.android.parcel.Parcelize

/**
 * @author GuangNian
 * @description:
 * @date : 2022/3/8 11:23 上午
 */
data class ChapterContent(
    var content: String="",
    val name: String="",
    val say: String="",
    val uptime:Long=0L,
    val cid:String="",
    val vcid:String="",
    var vcName:String ="选择卷",
    var volume:ArrayList<Volume> = ArrayList(),
    val st: Int=0
): BaseObservable(){
//    init {
//        volume.forEach { v->
//            if(v.cid.toString()==vcid){
//                v.select =true
//                obVcName = v.name
//            }
//        }
//    }

    @get:Bindable
    @set:Bindable
    var obVcid: String = vcid
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    @set:Bindable
    var obVcName: String = vcName
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    @set:Bindable
    var obName: String = name
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    @set:Bindable
    var obContent: String = content
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    @set:Bindable
    var obSay: String = say
        set(value) {
            field = value
            notifyChange()
        }
}

@Suppress("DEPRECATION")
@SuppressLint("ParcelCreator")
@Parcelize
data class Volume(val cid:Int=0,
                  var select:Boolean = false,
                  var name:String=""): Parcelable