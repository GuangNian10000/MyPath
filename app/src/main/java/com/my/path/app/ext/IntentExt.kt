package com.my.path.app.ext

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.my.path.R
import com.my.path.app.config.SysConfig
import com.my.path.app.config.SysConfig.CZXIY
import com.my.path.app.config.SysConfig.FWUXIY
import com.my.path.app.config.SysConfig.YINSI
import com.my.path.app.eventViewModel
import com.my.path.data.model.bean.*
import com.my.path.ui.activity.InLoginActivity
import me.guangnian.mvvm.ext.nav
import me.guangnian.mvvm.ext.navigateAction
import me.jessyan.retrofiturlmanager.RetrofitUrlManager


/**
 * @author GuangNian
 * @description:
 * @date : 2022/8/1 5:25 下午
 */

fun inAppUser(uid:Int){
    eventViewModel.inAppFragment.value = InApp(1,0,uid,0)
}

fun inAppRed(rid:Int){
    eventViewModel.inAppFragment.value = InApp(2,0,0,rid)
}

fun inAppBook(bookId:Int){
    eventViewModel.inAppFragment.value = InApp(3,bookId,0,0)
}

fun Activity.inBrowser(){
    val intent = Intent()
    intent.action = "android.intent.action.VIEW"
    val content_url: Uri = Uri.parse( if(getLanguage()) "https://z.dlxk.com/" else "https://m.dlxk.com/")
    intent.data = content_url
    startActivity(intent)
}


fun Fragment.inLoginCodeFragment(){
    nav().navigateAction(R.id.loginCodeFragment)
}

fun Fragment.inForgetPwdFragment(){
    nav().navigateAction(R.id.forgetPwdFragment)
}

fun Fragment.inForgetEmailFragment(){
    nav().navigateAction(R.id.forgetEmailFragment)
}

fun Fragment.inRegisteredEmailFragment(){
    nav().navigateAction(R.id.registeredEmailFragment)
}

fun Fragment.inRegisteredFragment(){
    nav().navigateAction(R.id.registeredFragment)
}

fun Fragment.inCalendarFragment(){
    nav().navigateAction(R.id.calendarFragment)
}

fun Fragment.inDataPaymentFragment(baseData: BaseData){
    nav().navigateAction(R.id.dataPaymentFragment, Bundle().apply {
        putParcelable("baseData", baseData)
    })
}

fun Fragment.inDataEarningsFragment(baseData: BaseData){
    nav().navigateAction(R.id.dataEarningsFragment, Bundle().apply {
        putParcelable("baseData", baseData)
    })
}


fun Fragment.inDataSituationFragment(baseData: BaseData){
    nav().navigateAction(R.id.dataSituationFragment, Bundle().apply {
        putParcelable("baseData", baseData)
    })
}

fun Fragment.inWritingChangesFragment(novel:Novel){
    nav().navigateAction(R.id.writingChangesFragment, Bundle().apply {
        putParcelable("novel", novel)
    })
}


fun Fragment.inRedListFragment(){
    nav().navigateAction(R.id.redListFragment)
}

fun Fragment.inCommentDetailsFragment(bid:Int,id:Int){
    nav().navigateAction(R.id.commentDetailsFragment, Bundle().apply {
        putInt("bid", bid)
        putInt("id", id)
    })
}

fun Fragment.inCommentListFragment(){
    nav().navigateAction(R.id.commentListFragment)
}

fun Activity.inInLoginFragment(){
    startActivity(Intent(this, InLoginActivity::class.java))
}


fun Fragment.showImg(url:String){
    val list :ArrayList<String> = ArrayList()
    list.add(url)
    inPictureViewerFragment(list)
}

fun View.showImg(url:String){
    val list :ArrayList<String> = ArrayList()
    list.add(url)
    inPictureViewerFragment(list)
}

fun Fragment.inPictureViewerFragment(list:ArrayList<String>,currentPosition:Int=0){
    nav().navigateAction(R.id.pictureViewerFragment, Bundle().apply {
        putStringArrayList("images", list)
        putInt("currentPosition", currentPosition)
    })
}

fun View.inPictureViewerFragment(list:ArrayList<String>,currentPosition:Int=0){
    nav(this).navigateAction(R.id.pictureViewerFragment, Bundle().apply {
        putStringArrayList("images", list)
        putInt("currentPosition", currentPosition)
    })
}

fun Fragment.inLoginFragment(){
    nav().navigateAction(R.id.loginFragment)
}

fun Fragment.inWritingAddFragment(){
    nav().navigateAction(R.id.writingAddFragment)
}

fun Fragment.inChapterListFragment(novel:Novel){
    nav().navigateAction(R.id.chapterListFragment, Bundle().apply {
        putParcelable("novel", novel)
    })
}

fun Fragment.inChapterAddFragment(bid: Int,list:ArrayList<ChapterData>,chapterData: ChapterData=ChapterData()){
    //筛选出卷
    val mVolumeList :ArrayList<Volume> = ArrayList()
    list.filter {
        it.type==1
    }.forEach {
        mVolumeList.add(Volume(it.cid,false,it.name))
    }

    nav().navigateAction(R.id.chapterAddFragment, Bundle().apply {
        putParcelable("chapterData", chapterData)
        putParcelableArrayList("mVolumeList", mVolumeList)
        putInt("bid",bid)
    })
}

fun Fragment.inChapterAddFragment(bid: Int, chapterData: ChapterData=ChapterData()){
    nav().navigateAction(R.id.chapterAddFragment, Bundle().apply {
        putParcelable("chapterData", chapterData)
        putInt("bid",bid)
    })
}

fun Fragment.inTicketListFragment(){
    nav().navigateAction(R.id.ticketListFragment)
}

fun Fragment.inGiftListFragment(){
    nav().navigateAction(R.id.giftListFragment)
}


fun View.inWebFragment(type:String){
    var title = ""
    var url = ""

    when(type){
        YINSI->{
            title = "隐私协议"
            url =  RetrofitUrlManager.getInstance().globalDomain.toString() + SysConfig.yinSiUrl
        }
        FWUXIY->{
            title = "用户服务协议"
            url =  RetrofitUrlManager.getInstance().globalDomain.toString() + SysConfig.serviceAgreementUrl
        }
        CZXIY->{
            title = "充值协议"
            url =  RetrofitUrlManager.getInstance().globalDomain.toString() + SysConfig.topUptUrl
        }
    }

    nav(this).navigateAction(R.id.webFragment, Bundle().apply {
        putString("title",title)
        putString("url",url)})
}
fun Fragment.inWebFragment(type:String){
    var title = ""
    var url = ""

    when(type){
        YINSI->{
            title = "隐私协议"
            url =  RetrofitUrlManager.getInstance().globalDomain.toString() + SysConfig.yinSiUrl
        }
        FWUXIY->{
            title = "用户服务协议"
            url =  RetrofitUrlManager.getInstance().globalDomain.toString() + SysConfig.serviceAgreementUrl
        }
        CZXIY->{
            title = "充值协议"
            url =  RetrofitUrlManager.getInstance().globalDomain.toString() + SysConfig.topUptUrl
        }
    }

    nav().navigateAction(R.id.webFragment, Bundle().apply {
        putString("title",title)
        putString("url",url)})
}