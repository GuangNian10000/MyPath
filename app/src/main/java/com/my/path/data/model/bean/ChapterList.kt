package com.my.path.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import com.my.path.R
import com.my.path.app.util.TimeUtil.timestampToDate
import com.drake.brv.item.ItemExpand
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.IgnoredOnParcel
import me.guangnian.mvvm.base.appContext

/**
 * @author GuangNian
 * @description:
 * @date : 2022/3/4 5:45 下午
 */
@Suppress("DEPRECATION")
@SuppressLint("ParcelCreator")
@Parcelize

data class ChapterList(
    val `data`: ArrayList<ChapterData> = ArrayList(),
    val public:Int =0,
    val draft:Int=0,
    val st: Int=0
): Parcelable

@Suppress("DEPRECATION")
@SuppressLint("ParcelCreator")
@Parcelize
data class ChapterData(
    val addtime: Long=0L,
    val cid: Int=0,
    val islock: String="",
    val isvip: Int=0,
    var name: String="",
    var order: Int=0,
    val price: Int=0,
    val size: Int= 0,
    var timing: Long=0L,
    val type: Int=0,
    var state:Int=2,// "string,0公开，2草稿，3锁定"
    val uptime: Long=0L, override var theOrder: Int = order
): Parcelable, ItemExpand,ChapterAllData {
    fun getPerSize():String{
        return if(0==price){
            size.toString()+appContext.getString(
                R.string.zijhdjashe)
        }else{
            price.toString()+ appContext.getString(R.string.xingkjdkoasje)+size+appContext.getString(
                R.string.zijhdjashe)
        }
    }

    fun getDTime()= if(0L==timing){
            appContext.getString(R.string.dingshigonbdjakse)
        }else{
            timestampToDate(timing)
        }

    fun getXTime()=   timestampToDate(addtime)

    @IgnoredOnParcel
    override var itemGroupPosition: Int = 0
    @IgnoredOnParcel
    override var itemExpand: Boolean = false
    @IgnoredOnParcel
    override var itemSublist: List<Any?>? = null
    @IgnoredOnParcel
    var chapterData:ChapterData?=null

    @IgnoredOnParcel
    var lines :Boolean = true
}
interface ChapterAllData{
    var theOrder: Int
}