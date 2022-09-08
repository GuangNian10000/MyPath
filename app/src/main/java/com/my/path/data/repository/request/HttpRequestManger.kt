package com.my.path.data.repository.request

import android.view.View
import com.my.path.app.ext.getImageBody
import com.my.path.app.ext.getStrBody
import com.my.path.app.ext.makeToast
import com.my.path.app.network.apiService
import com.my.path.app.util.BCConvert.bj2qj
import com.my.path.app.util.CacheUtil
import com.my.path.data.model.bean.*
import com.google.firebase.perf.FirebasePerformance
import me.guangnian.mvvm.network.AppException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


/**
 * 作者　: GuangNian
 * 时间　: 2020/5/4
 * 描述　: 处理协程的请求类
 */

val HttpRequestCoroutine: HttpRequestManger by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    HttpRequestManger()
}

class HttpRequestManger {

    private fun <T> reBean(st :Int,result:String,msg:String,data:T):T{
        if(st==200){
            makeToast(result)
            return data
        }else{
            //抛出错误异常
            makeToast(msg)
            throw AppException(st,"")
        }
    }

    /**
     * 码字日历
     * */
    suspend fun meUpdate(bid:String,year:String,month:String): CalendarCode {
        val meUpdate = apiService.meUpdate(bid,year,month)
        //判断注册结果 注册成功，调用登录接口
        if(meUpdate.st==200){
            meUpdate.year = year.toInt()
            meUpdate.month = month.toInt()
            return meUpdate
        }else {
            //抛出错误异常
            throw AppException(meUpdate.st,"")
        }
    }

    //登陆
    suspend fun login(username: String, password: String): LoginInfo {
        val bean = apiService.login(username, password)
        if(bean.st==200){
            CacheUtil.setLoginInfo(bean) //存储用户信息
            makeToast(bean.result)
            return bean
        }else{
            //抛出错误异常
            makeToast(bean.msg)
            throw AppException(bean.st,"")
        }
    }

    //作品列表
    suspend fun authorIndex(): ArrayList<Novel> {
        val bean = apiService.authorIndex()
        //判断注册结果 注册成功，调用登录接口
        if(bean.st==200){
            return bean.novel
        }else {
            //抛出错误异常
            if(bean.msg!="请登录"){
                makeToast(bean.msg)
            }
            throw AppException(bean.st,bean.msg)
        }
    }

    suspend fun authorBookdel(bid:Int):BaseCode{
        val tipIndex = apiService.authorBookdel(bid)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            makeToast(tipIndex.result)
            return tipIndex
        }else {
            //抛出错误异常
            makeToast(tipIndex.msg)
            throw AppException(tipIndex.st,tipIndex.msg)
        }
    }

    suspend fun firebasePerformance(){
        val performance = FirebasePerformance.getInstance()
        performance.isPerformanceCollectionEnabled = true
    }

    //创建作品
    suspend fun authorCreate(name:String,
                             intro:String,
                             yc:Int,
                             lx:Int,
                             xx:Int,
                             sj:Int,
                             sd:Int,
                             fg:Int): BaseCode {
        val tipIndex = apiService.authorCreate(name,intro,yc,lx,xx,sj,sd,fg)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            makeToast(tipIndex.result)
            return tipIndex
        }else {
            //抛出错误异常
            makeToast(tipIndex.msg)
            throw AppException(tipIndex.st,"")
        }
    }

    suspend fun authorTag(): ArrayList<Tag>{
        val tipIndex = apiService.authorTag()
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            return tipIndex.tags
        }else {
            //抛出错误异常
            throw AppException(tipIndex.st,"")
        }
    }

    suspend fun authorEditBook(bid:Int,
                               intro:String,
                               remark:String,
                               yc:Int,
                               lx:Int,
                               xx:Int,
                               sj:Int,
                               sd:Int,
                               fg:Int,
                               zt:String,
                               tid:String): BaseCode {
        val tipIndex = apiService.authorEditBook(bid,intro,remark,yc,lx,xx,sj,sd,fg,zt,tid)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            makeToast(tipIndex.result)
            return tipIndex
        }else{
            //抛出错误异常
            makeToast(tipIndex.msg)
            throw AppException(tipIndex.st,"")
        }
    }

    suspend fun authorZt():ArrayList<Tag>{
        val tipIndex = apiService.authorZt()
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            return tipIndex.zt
        }else {
            //抛出错误异常
            throw AppException(tipIndex.st,"")
        }
    }

    suspend fun authorYclx(): ArrayList<Yc> {
        val bean = apiService.authorYclx()
        //判断注册结果 注册成功，调用登录接口
        if(bean.st==200){
            return bean.yc
        }else {
            //抛出错误异常
            throw AppException(bean.st,"")
        }
    }

    suspend fun authorXxsj(): ArrayList<Yc> {
        val bean = apiService.authorXxsj()
        //判断注册结果 注册成功，调用登录接口
        if(bean.st==200){
            return bean.xx
        }else {
            //抛出错误异常
            throw AppException(bean.st,"")
        }
    }

    suspend fun authorCover(url:String,bid:Int,lang:String):UploadAvatarFile{
        val file =  File(url)
        val fileRQ = RequestBody.create("image/*".toMediaTypeOrNull(),file);
        val part = MultipartBody.Part.createFormData("img", file.name, fileRQ)

        val  bidBody = RequestBody.create("text/plain".toMediaTypeOrNull(), bid.toString())
        val  langBody = RequestBody.create("text/plain".toMediaTypeOrNull(), lang.toString())

        val tipIndex = apiService.authorCover(part,bidBody,langBody)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            makeToast(tipIndex.result)
            return tipIndex
        }else {
            //抛出错误异常
            makeToast(tipIndex.msg)
            throw AppException(tipIndex.st,"")
        }
    }

    suspend fun authorSdfg(): ArrayList<Yc> {
        val bean = apiService.authorSdfg()
        //判断注册结果 注册成功，调用登录接口
        if(bean.st==200){
            return bean.sd
        }else {
            //抛出错误异常
            throw AppException(bean.st,"")
        }
    }

    suspend fun authorSavechapter(bid:Int,
                                  chaptername:String,
                                  content:String,
                                  say:String,
                                  publish:Int,
                                  volume:String,
                                  cid: String=""):SaveChapter{
        val tipIndex = apiService.authorSavechapter(bid,chaptername,content,say,publish,volume,cid)
        //判断注册结果 注册成功，调用登录接口
        return tipIndex
//        if(tipIndex.st==200){
//            return tipIndex
//        }else {
//            //抛出错误异常
//            throw AppException(tipIndex.st,"")
//        }
    }

    suspend fun authorChapterlist(bid:Int): ChapterList {
        val bean = apiService.authorChapterlist(bid)
        //判断注册结果 注册成功，调用登录接口
        if(bean.st==200){
            return bean
        }else {
            //抛出错误异常
            throw AppException(bean.st,"")
        }
    }

    suspend fun authorChapterDraftlist(bid:Int): ChapterList {
        val bean = apiService.authorChapterDraftlist(bid)
        //判断注册结果 注册成功，调用登录接口
        if(bean.st==200){
            return bean
        }else {
            //抛出错误异常
            throw AppException(bean.st,"")
        }
    }

    suspend fun authorEditchapterename(position:Int,bid:Int,cid:Int,chaptername:String):BaseCode{
        val bean = apiService.authorEditchapterename(bid,cid, chaptername)
        //判断注册结果 注册成功，调用登录接口
        if(bean.st==200){
            bean.position = position
            bean.additional = chaptername
            return bean
        }else {
            //抛出错误异常
            throw AppException(bean.st,bean.msg)
        }
    }

    suspend fun tipIndex(): TipIndex {
        val tipIndex = apiService.tipIndex()
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            return tipIndex
        }else {
            //抛出错误异常
            throw AppException(tipIndex.st,"")
        }
    }

    suspend fun tipReadall(): BaseCode {
        val tipIndex = apiService.tipReadall()
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            return tipIndex
        }else {
            //抛出错误异常
            throw AppException(tipIndex.st,"")
        }
    }

    suspend fun tipTicket(bid:Int,page:Int):  ArrayList<TipTicketData> {
        val tipIndex = apiService.tipTicket(bid,page)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            return tipIndex.data
        }else {
            //抛出错误异常
            throw AppException(tipIndex.st,"")
        }
    }

    suspend fun tipHongbao(bid:Int,page:Int):  ArrayList<HongBao> {
        val tipIndex = apiService.tipHongbao(bid,page)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            return tipIndex.data
        }else {
            //抛出错误异常
            throw AppException(tipIndex.st,"")
        }
    }


    suspend fun tipGift(bid:Int,page:Int): ArrayList<TipGiftData> {
        val tipIndex = apiService.tipGift(bid,page)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            return tipIndex.data
        }else {
            //抛出错误异常
            throw AppException(tipIndex.st,"")
        }
    }



    /**
     * 获取作品
     * */
    suspend fun meGetbook(): MeGetBook {
        val meGetbook = apiService.meGetbook()
        //判断注册结果 注册成功，调用登录接口
        if(meGetbook.st==200){
            return meGetbook
        }else {
            //抛出错误异常
            throw AppException(meGetbook.st,"")
        }
    }

    /**
     * 获取个人信息
     * */
    suspend fun meGetInFo(): UserData {
        val meGetInFo = apiService.meGetInFo()
        //判断注册结果 注册成功，调用登录接口
        if(meGetInFo.st==200){
            CacheUtil.setUserData(meGetInFo)
            return meGetInFo
        }else {
            //抛出错误异常
            throw AppException(meGetInFo.st, "")
        }
    }

    /**
     * 修改个人信息
     * */
    suspend fun meSaveinfo(meGetInFo:UserData): BaseCode {
        val meGetInFo = apiService.meSaveinfo(meGetInFo.nick, meGetInFo.sign,meGetInFo.sex.toString(),meGetInFo.year.toString(),meGetInFo.month.toString(),meGetInFo.day.toString())
        //判断注册结果 注册成功，调用登录接口
        if(meGetInFo.st==200){
            return meGetInFo
        }else {
            //抛出错误异常
            throw AppException(meGetInFo.st, "")
        }
    }


    /**
     * 修改个人信息
     * */
    suspend fun meUploadAvatarfile(url:String): UploadAvatarFile {
        val file =  File(url)
        val fileRQ = RequestBody.create("image/*".toMediaTypeOrNull(),file);
        val part = MultipartBody.Part.createFormData("avatar", file.name, fileRQ);
        val meUploadAvatarfile = apiService.meUploadAvatarfile(part)
        //判断注册结果 注册成功，调用登录接口
        if(meUploadAvatarfile.st==200){
            return meUploadAvatarfile
        }else {
            //抛出错误异常
            throw AppException(meUploadAvatarfile.st, "")
        }
    }



    /**
     * 修改笔名
     * */
    suspend fun meSaveauthor(author:String): BaseCode {
        val meGetInFo = apiService.meSaveauthor(author)
        //判断注册结果 注册成功，调用登录接口
        if(meGetInFo.st==200){
            return meGetInFo
        }else {
            //抛出错误异常
            throw AppException(meGetInFo.st, "")
        }
    }

    //评论
    suspend fun commentReplyPost(_id:Int,content:String,file:String=""): ReplyPost {
        val tipIndex =
            if(""==file)
                apiService.commentReplyPost(_id,content)
            else
                apiService.commentReplyPost(getImageBody(File(file)),
                    getStrBody(_id.toString()),
                    getStrBody(content))
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            makeToast(tipIndex.result)
            return tipIndex
        }else {
            //抛出错误异常
            makeToast(tipIndex.msg)
            throw AppException(tipIndex.st,"")
        }
    }

    suspend fun commentPost(_id:Int,content:String,file:String="",chapterid: String = ""): ReplyPost {
        val tipIndex =
            if(""==file)
                apiService.commentPost(_id,chapterid,content)
            else
                apiService.commentPost(
                    getImageBody(File(file)),
                    getStrBody(_id.toString()),
                    getStrBody(content),
                    getStrBody(chapterid))
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            makeToast(tipIndex.result)
            return tipIndex
        }else {
            //抛出错误异常
            makeToast(tipIndex.msg)
            throw AppException(tipIndex.st,"")
        }
    }

    //获取评论详情
    suspend fun commentDetails(_id:Int,cid:Int): PostHead {
        val tipIndex = apiService.commentDetails(_id,cid)
        return tipIndex
    }

    //评论点赞
    suspend fun commentLiked(_id:Int,op:Int): BaseCode {
        val tipIndex = apiService.commentLiked(_id,op)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            makeToast(tipIndex.result)
            return tipIndex
        }else {
            //抛出错误异常
            makeToast(tipIndex.msg)
            throw AppException(tipIndex.st,"")
        }
    }

    //评论回复点赞
    suspend fun commentReplyLiked(_id:Int,op:Int): BaseCode {
        val tipIndex = apiService.commentReplyLiked(_id,op)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            makeToast(tipIndex.result)
            return tipIndex
        }else {
            //抛出错误异常
            makeToast(tipIndex.msg)
            throw AppException(tipIndex.st,"")
        }
    }

    //评论删除
    suspend fun commentDel(_id:Int,pos:Int): BaseCode {
        val tipIndex = apiService.commentDel(_id)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            tipIndex.position = pos
            makeToast(tipIndex.result)
            return tipIndex
        }else {
            //抛出错误异常
            makeToast(tipIndex.msg)
            throw AppException(tipIndex.st,"")
        }
    }

    //设置精华
    suspend fun commentGood(position:Int,state:Int,_id:Int): BaseCode {
        val tipIndex = apiService.commentGood(_id)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            tipIndex.position = position
            tipIndex.additional = state.toString()
            makeToast(tipIndex.result)
            return tipIndex
        }else {
            //抛出错误异常
            makeToast(tipIndex.msg)
            throw AppException(tipIndex.st,tipIndex.msg)
        }
    }

    //置顶
    suspend fun commentTop(_id:Int): BaseCode {
        val tipIndex = apiService.commentTop(_id)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            makeToast(tipIndex.result)
            return tipIndex
        } else {
            //抛出错误异常
            makeToast(tipIndex.msg)
            throw AppException(tipIndex.st,"")
        }
    }

    suspend fun commentList(bid:Int,type:String,page:Int): CommentList {
        val tipIndex = apiService.commentList(bid,page,type)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            return tipIndex
        }else {
            //抛出错误异常
            throw AppException(tipIndex.st,"")
        }
    }

    //评论详情的回复列表
    suspend fun commentReply(_id:Int,page:Int): CommentList {
        val tipIndex = apiService.commentReply(_id,page)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            return tipIndex
        }else {
            //抛出错误异常

            throw AppException(tipIndex.st,"")
        }
    }

    suspend fun authorGetchapter(bid:Int,cid:Int):ChapterContent{
        val tipIndex = apiService.authorGetchapter(bid,cid)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            tipIndex.volume.forEach { v->
                    if(v.cid.toString()==tipIndex.vcid){
                        v.select = true
                        tipIndex.obVcName = v.name
                        tipIndex.obVcid = v.cid.toString()
                    }
                }
            bj2qj(tipIndex.content.replace("    ","  "))?.let {
                tipIndex.obContent =it
            }

            return tipIndex
        }else {
            //抛出错误异常

            throw AppException(tipIndex.st,"")
        }
    }

    suspend fun authorChapterDel(bid:Int,cid:Int,position:Int):BaseCode{
        val tipIndex = apiService.authorChapterDel(bid,cid)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            tipIndex.position = position
            return tipIndex
        }else {
            //抛出错误异常
            makeToast(tipIndex.msg)
            throw AppException(tipIndex.st,tipIndex.msg)
        }
    }

    suspend fun authorChapterPublish(bid:Int,cid:Int,position:Int):BaseCode{
        val tipIndex = apiService.authorChapterPublish(bid,cid)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            makeToast(tipIndex.result)
            tipIndex.position = position
            return tipIndex
        }else {
            //抛出错误异常
            makeToast(tipIndex.msg)
            throw AppException(tipIndex.st,tipIndex.msg)
        }
    }

    suspend fun authorChaptermoveup(bid:Int,cid:Int,order:Int,position:Int):BaseCode{
        val tipIndex = apiService.authorChaptermoveup(bid,cid,order)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            tipIndex.position = position
            return tipIndex
        }else {
            //抛出错误异常
            throw AppException(tipIndex.st,tipIndex.msg)
        }
    }

    suspend fun authorChaptermovedown(bid:Int,cid:Int,order:Int,position:Int):BaseCode{
        val tipIndex = apiService.authorChaptermovedown(bid,cid,order)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            tipIndex.position = position
            return tipIndex
        }else {
            //抛出错误异常
            throw AppException(tipIndex.st,tipIndex.msg)
        }
    }

    suspend fun authorChaptertiming(position:Int,bid:Int,cid:Int,timing:String):BaseCode{
        val tipIndex = apiService.authorChaptertiming(bid,cid,timing)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            tipIndex.additional = timing
            tipIndex.position = position
            return tipIndex
        }else {
            //抛出错误异常
            throw AppException(tipIndex.st,tipIndex.msg)
        }
    }

    suspend fun dataBase():DataBase{
        val tipIndex = apiService.dataBase()
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            return tipIndex
        }else {
            //抛出错误异常
            throw AppException(tipIndex.st,"")
        }
    }

    suspend fun dataSurvey(bid:Int):DataSurvey{
        val tipIndex = apiService.dataSurvey(bid)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){

            return tipIndex
        }else {
            //抛出错误异常
            throw AppException(tipIndex.st,"")
        }
    }

    suspend fun dataSubscribeDetail(bid:Int,page: Int):ArrayList<DataSubscribe>{
        val tipIndex = apiService.dataSubscribeDetail(bid,page)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            return tipIndex.data
        }else {
            //抛出错误异常
            throw AppException(tipIndex.st,"")
        }
    }

    suspend fun dataGift(bid:Int):DataGift{
        val tipIndex = apiService.dataGift(bid)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            return tipIndex
        }else {
            //抛出错误异常
            throw AppException(tipIndex.st,"")
        }
    }

    suspend fun dataSubscribe(bid:Int):DataSub{
        val tipIndex = apiService.dataSubscribe(bid)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            return tipIndex
        }else {
            //抛出错误异常
            throw AppException(tipIndex.st,"")
        }
    }

    suspend fun dataIncome(bid:Int):DataIncome{
        val tipIndex = apiService.dataIncome(bid)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            return tipIndex
        }else {
            //抛出错误异常
            throw AppException(tipIndex.st,"")
        }
    }

    suspend fun dataPayment(bid:Int):Payment{
        val tipIndex = apiService.dataPayment(bid)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            return tipIndex
        }else {
            //抛出错误异常
            throw AppException(tipIndex.st,"")
        }
    }


    /**
     * 注册
     */
    suspend fun register(country: String, phone: String, code: String, password: String, nick: String): LoginInfo {
        val registerData = apiService.register(country, phone, code,password)
        //判断注册结果 注册成功，调用登录接口
        if (registerData.st==200) {
            CacheUtil.setLoginInfo(registerData) //存储用户信息
            return registerData
        } else {
            //抛出错误异常
            makeToast(registerData.msg)
            throw AppException(registerData.st, registerData.msg)
        }
    }

    /**
     * 邮箱注册
     */
    suspend fun registerEmail(email: String, code: String,password:String,nick:String): LoginInfo {
        val registerData = apiService.registerEmail(email, code, password)
        //判断注册结果 注册成功，调用登录接口
        if (registerData.st==200) {
            CacheUtil.setLoginInfo(registerData) //存储用户信息
            return registerData
        } else {
            //抛出错误异常
            makeToast(registerData.msg)
            throw AppException(registerData.st, registerData.msg)
        }
    }

    suspend fun editpassEmail(email: String, code: String,password:String,repassword:String): LoginInfo {
        val registerData = apiService.editpassEmail(email, code, password,repassword)
        //判断注册结果 注册成功，调用登录接口
        if (registerData.st==200) {
            CacheUtil.setLoginInfo(registerData) //存储用户信息
            return registerData
        } else {
            //抛出错误异常
            makeToast(registerData.msg)
            throw AppException(registerData.st, registerData.msg)
        }
    }

    /**
     * 邮箱验证码
     */
    suspend fun registerSendemail(username: String): BaseCode {
        val registerData = apiService.registerSendemail(username)
        //判断注册结果 注册成功，调用登录接口
        if (registerData.st==200) {
            makeToast(registerData.result)
            return registerData
        } else {
            //抛出错误异常
            makeToast(registerData.msg)
            throw AppException(registerData.st, registerData.msg)
        }
    }


    /**
     * 验证码登录
     */
    suspend fun loginCode(country:String,phone:String,code:String): LoginInfo {
        val loginCode = apiService.loginCode(country, phone,code)
        //判断注册结果 注册成功，调用登录接口
        if (loginCode.st==200) {
            CacheUtil.setLoginInfo(loginCode) //存储用户信息
            return loginCode
        } else {
            //抛出错误异常
            makeToast(loginCode.msg)
            throw AppException(loginCode.st, loginCode.msg)
        }
    }

    /**
     * 获取国别码
     */
    suspend fun ip2country(): CountryEntity {
        val registerData = apiService.ip2country()
        //判断注册结果 注册成功，调用登录接口
        if(registerData.st==200){
            return registerData
        }else {
            //抛出错误异常
            throw AppException(registerData.st, "")
        }
    }

    /**
     * 发送验证码
     * */
    suspend fun registerSendcode(country:String,phone:String): BaseCode {
        val BaseCode = apiService.registerSendcode(country,phone)
        //判断注册结果 注册成功，调用登录接口
        if(BaseCode.st==200){
            makeToast(BaseCode.result)
            return BaseCode
        }else {
            //抛出错误异常
            makeToast(BaseCode.msg)
            throw AppException(BaseCode.st, BaseCode.msg)
        }
    }

    suspend fun sendbindemail(phone:String): BaseCode {
        val BaseCode = apiService.sendbindemail(phone)
        //判断注册结果 注册成功，调用登录接口
        if(BaseCode.st==200){
            makeToast(BaseCode.result)
            return BaseCode
        }else {
            //抛出错误异常
            makeToast(BaseCode.msg)
            throw AppException(BaseCode.st, BaseCode.msg)
        }
    }


    suspend fun loginSendcode(country:String,phone:String): BaseCode {
        val BaseCode = apiService.loginSendcode(country,phone)
        //判断注册结果 注册成功，调用登录接口
        if(BaseCode.st==200){
            makeToast(BaseCode.result)
            return BaseCode
        }else {
            //抛出错误异常
            makeToast(BaseCode.msg)
            throw AppException(BaseCode.st, BaseCode.msg)
        }
    }


    /**
     * 发送验证码
     * */
    suspend fun registerSendvoice(country:String,phone:String): BaseCode {
        val BaseCode = apiService.registerSendvoice(country,phone)
        //判断注册结果 注册成功，调用登录接口
        if(BaseCode.st==200){
            makeToast(BaseCode.result)
            return BaseCode
        }else {
            //抛出错误异常
            makeToast(BaseCode.msg)
            throw AppException(BaseCode.st, BaseCode.msg)
        }
    }

    suspend fun sendvoice(country:String,phone:String): BaseCode {
        val BaseCode = apiService.sendvoice(country,phone)
        //判断注册结果 注册成功，调用登录接口
        if(BaseCode.st==200){
            makeToast(BaseCode.result)
            return BaseCode
        }else {
            //抛出错误异常
            makeToast(BaseCode.msg)
            throw AppException(BaseCode.st, BaseCode.msg)
        }
    }

    suspend fun registerOnekey(tk:String): LoginInfo {
        val tipIndex = apiService.registerOnekey(tk)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            CacheUtil.setLoginInfo(tipIndex) //存储用户信息
            return tipIndex
        }else {
            makeToast(tipIndex.msg)
            //抛出错误异常
            throw AppException(tipIndex.st,"")
        }
    }


    /**
     * 修改密码
     * */
    suspend fun editpass(country:String,phone:String,code:String,password:String,repassword:String): LoginInfo {
        val userInfo = apiService.editpass(country,phone,code,password,repassword)
        //判断注册结果 注册成功，调用登录接口
        if(userInfo.st==200){
            CacheUtil.setLoginInfo(userInfo) //存储用户信息

            return userInfo
        }else {
            //抛出错误异常
            makeToast(userInfo.msg)
            throw AppException(userInfo.st, userInfo.msg)
        }
    }


    /**
     * 提交意见反馈
     * */
    suspend fun feedbackAdd(content:String,urls:String): FeedBackAdd {
        val feedbackAdd = apiService.feedbackAdd(content,urls)
        //判断注册结果 注册成功，调用登录接口
        if(feedbackAdd.st==200){
            return feedbackAdd
        }else {
            //抛出错误异常
            throw AppException(feedbackAdd.st, feedbackAdd.msg)
        }
    }

    /**
     * 上传图片
     * */
    suspend fun feedbackUpimg(listPicture:MutableList<Picture>): MutableList<Picture> {
        for(f in listPicture){
            if(null==f.photo){
                continue
            }
            val file =  File(f.photo!!.path)
            val fileRQ = RequestBody.create("image/*".toMediaTypeOrNull(),file);
            val part = MultipartBody.Part.createFormData("img", file.name, fileRQ);
            val feedbackAdd = apiService.feedbackUpimg(part)

            if(feedbackAdd.st==200){
                f.serviceUrl=feedbackAdd.url
                f.view?.visibility = View.GONE
            }else {
                f.serviceUrl=null
                //抛出错误异常
                throw AppException(feedbackAdd.st, feedbackAdd.msg)
            }
        }
        return listPicture
    }

    /**
     * 绑定手机号
     * */
    suspend fun registerBind(country: String, phone: String,code:String): BaseCode {
        val baseCode = apiService.registerBind(country,phone,code)
        //判断注册结果 注册成功，调用登录接口
        if(baseCode.st==200){
            makeToast(baseCode.result)
            return baseCode
        }else {
            //抛出错误异常
            makeToast(baseCode.msg)
            throw AppException(baseCode.st,"")
        }
    }
    /**
     * 绑定邮箱
     * */
    suspend fun registerBindemail(email: String,code:String): BaseCode {
        val baseCode = apiService.registerBindemail(email,code)
        //判断注册结果 注册成功，调用登录接口
        if(baseCode.st==200){
            return baseCode
        }else {
            //抛出错误异常
            throw AppException(baseCode.st,"")
        }
    }

    suspend fun meEditpass(oldpass:String,newpass:String): BaseCode {
        val baseCode = apiService.meEditpass(oldpass,newpass)
        //判断注册结果 注册成功，调用登录接口
        if(baseCode.st==200){
            return baseCode
        }else {
            //抛出错误异常
            throw AppException(baseCode.st,"")
        }
    }

    /**
     * 退出登录
     * */
    suspend fun logout(): BaseCode {
        val baseCode = apiService.logout()
        //判断注册结果 注册成功，调用登录接口
        if(baseCode.st==200){
            return baseCode
        }else {
            //抛出错误异常
            throw AppException(baseCode.st,"")
        }
    }


    suspend fun flowBlacklist(page:Int): FlowBlack {
        val tipIndex = apiService.flowBlacklist(page)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            return tipIndex
        }else {
            //抛出错误异常
            throw AppException(tipIndex.st,"")
        }
    }

    suspend fun flowRemoveblacklist(position:Int,did:Int): BaseCode {
        val tipIndex = apiService.flowRemoveblacklist(did)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            tipIndex.position = position
            return tipIndex
        }else {
            //抛出错误异常
            throw AppException(tipIndex.st,"")
        }
    }

    suspend fun flowAddblacklist(did:Int): BaseCode {
        val tipIndex = apiService.flowAddblacklist(did)
        //判断注册结果 注册成功，调用登录接口
        if(tipIndex.st==200){
            return tipIndex
        }else {
            //抛出错误异常
            throw AppException(tipIndex.st,"")
        }
    }
}