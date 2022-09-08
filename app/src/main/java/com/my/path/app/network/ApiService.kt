package com.my.path.app.network

import com.my.path.data.model.bean.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * 作者　: GuangNian
 * 时间　: 2019/12/23
 * 描述　: 网络API
 */
interface ApiService {

    companion object {
        const val SERVER_URL_CN = "https://zscn.dlxk.com/"
        const val SERVER_URL_TW = "https://zs.dlxk.com/"
    }


    /**
     * 码字日历
     */
    @FormUrlEncoded
    @POST("/me/update/")
    suspend fun meUpdate( @Field("bid") bid: String,
                          @Field("year") year:String,
                          @Field("month") month:String): CalendarCode


    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("/login/")
    suspend fun login(
        @Field("phone") username: String,
        @Field("password") pwd: String
    ): LoginInfo

    /**
     * 作品列表
     */
    @GET("/author/index/")
    suspend fun authorIndex(): AuthorBook

    /**
     * 创建作品
     */
    @FormUrlEncoded
    @POST("/author/create/")
    suspend fun authorCreate(@Field("name") name: String,
                             @Field("intro") intro: String,
                             @Field("yc") yc: Int,
                             @Field("lx") lx: Int,
                             @Field("xx") xx: Int,
                             @Field("sj") sj: Int,
                             @Field("sd") sd: Int,
                             @Field("fg") fg: Int): BaseCode
    /**
     * 删除书籍
     */
    @FormUrlEncoded
    @POST("/author/bookdel/")
    suspend fun authorBookdel(@Field("bid") bid: Int): BaseCode

    /**
     * 作品标签
     */
    @POST("/author/tag/")
    suspend fun authorTag(): AuthorTag


    /**
     * 修改作品信息
     */
    @FormUrlEncoded
    @POST("/author/editbook/")
    suspend fun authorEditBook(
        @Field("bid") bid: Int,
        @Field("intro") intro: String,
        @Field("remark") remark: String,
        @Field("yc") yc: Int,
        @Field("lx") lx: Int,
        @Field("xx") xx: Int,
        @Field("sj") sj: Int,
        @Field("sd") sd: Int,
        @Field("fg") fg: Int,
        @Field("zt") zt: String,
        @Field("tid") tid: String,
    ): BaseCode


    /**
     * 作品狀態
     */
    @POST("/author/zt/")
    suspend fun authorZt(): AuthorZt


    /**
     * 分类类型
     */
    @POST("/author/yclx/")
    suspend fun authorYclx(): AuthorYclx

    /**
     * 性向视角
     */
    @POST("/author/xxsj/")
    suspend fun authorXxsj(): AuthorXxsj

    /**
     * 时代风格
     */
    @POST("/author/sdfg/")
    suspend fun authorSdfg(): AuthorSdfg

    /**
     * 上传封面
     */
    @Multipart
    @POST("/author/cover/")
    suspend fun authorCover(@Part file: MultipartBody.Part,
                            @Part("bid")  bid: RequestBody,
                            @Part("lang")  lang:RequestBody): UploadAvatarFile


    //章节列表
    /**
     * 保存章节内容
     */
    @FormUrlEncoded
    @POST("/author/savechapter/")
    suspend fun authorSavechapter(@Field("bid") bid: Int,
                                  @Field("chaptername") chaptername: String,
                                  @Field("content") content: String,
                                  @Field("say") say: String,
                                  @Field("publish") publish: Int,
                                  @Field("volume") volume: String,
                                  @Field("cid") cid: String): SaveChapter

    /**
     * 所有章节
     * */
    @FormUrlEncoded
    @POST("/author/chapterlist/")// /author/chapterlist/
    suspend fun authorChapterlist(@Field("bid") bid: Int): ChapterList

    /**
     * 草稿章节
     * */
    @FormUrlEncoded
    @POST("/author/chapterlist/draft/")
    suspend fun authorChapterDraftlist(@Field("bid") bid: Int): ChapterList


    /**
     * 快捷修改章节名
     */
    @FormUrlEncoded
    @POST("/author/editchapterename/")
    suspend fun authorEditchapterename(@Field("bid") bid: Int,@Field("cid") cid: Int,
                                       @Field("chaptername") chaptername: String): BaseCode


    /**
     * 消息数量
     */
    @GET("/tip/index/")
    suspend fun tipIndex():TipIndex

    /**
     * 全部已读
     */
    @POST("/tip/readall/")
    suspend fun tipReadall():BaseCode

    /**
     * 推荐票记录
     */
    @FormUrlEncoded
    @POST("/tip/ticket/")
    suspend fun tipTicket(
        @Field("bid") bid: Int,
        @Field("page") page: Int
    ):TipTicket

    @FormUrlEncoded
    @POST("/tip/hongbao/")
    suspend fun tipHongbao(
        @Field("bid") bid: Int,
        @Field("page") page: Int
    ):HongBaoData


    /**
     * 打赏礼物记录
     */
    @FormUrlEncoded
    @POST("/tip/gift/")
    suspend fun tipGift(
        @Field("bid") bid: Int,
        @Field("page") page: Int
    ):TipGift


    /**
     * 获取作品
     */
    @GET("/me/getbook/")
    suspend fun meGetbook(): MeGetBook

    /**
     * 获取个人资料
     */
    @GET("/me/getinfo/")
    suspend fun meGetInFo(): UserData


    /**
     * 保存修改资料
     */
    @FormUrlEncoded
    @POST("/me/saveinfo/")
    suspend fun meSaveinfo(
        @Field("nick") nick: String,
        @Field("sign") sign: String,
        @Field("sex") sex: String,
        @Field("year") year: String,
        @Field("month") month: String,
        @Field("day") day: String
    ):BaseCode

    /**
     * 上传头像-直接传送file
     */
    @Multipart
    @POST("/me/upload/avatarfile/")
    suspend fun meUploadAvatarfile(@Part file: MultipartBody.Part): UploadAvatarFile


    /**
     * 修改笔名
     */
    @FormUrlEncoded
    @POST("/me/saveauthor/")
    suspend fun meSaveauthor(
        @Field("author") author: String,
    ):BaseCode


    /**
     * 保存评论
     */
    @FormUrlEncoded
    @POST("/comment/reply/post/")
    suspend fun commentReplyPost(
        @Field("_id") _id: Int,
        @Field("content") content: String
    ): ReplyPost

    /**
     * 回复评论
     */
    @Multipart
    @POST("/comment/reply/post/")
    suspend fun commentReplyPost(
        @Part file: MultipartBody.Part,
        @Part("_id") id: RequestBody,
        @Part("content") content: RequestBody
    ): ReplyPost

    /**
     * 发评论
     */
    @Multipart
    @POST("/comment/post/")
    suspend fun commentPost(
        @Part file: MultipartBody.Part,
        @Part("_id") id: RequestBody,
        @Part("content") content: RequestBody,
        @Part("chapterid") chapterid: RequestBody,
    ): ReplyPost


    /**
     * 保存评论
     */
    @FormUrlEncoded
    @POST("/comment/post/")
    suspend fun commentPost(
        @Field("_id") _id: Int,
        @Field("chapterid") chapterid: String,
        @Field("content") content: String
    ): ReplyPost

    /**
     * 评论详情的回复列表
     */
    @FormUrlEncoded
    @POST("/comment/reply/")
    suspend fun commentReply(
        @Field("_id") _id: Int,
        @Field("page") page: Int
    ): CommentList

    /**
     * 获取评论详情
     */
    @FormUrlEncoded
    @POST("/comment/detail/")
    suspend fun commentDetails(
        @Field("_id") bookId: Int,
        @Field("cid") commentsId: Int
    ): PostHead

    /**
     * 评论点赞
     */
    @FormUrlEncoded
    @POST("/comment/liked/")
    suspend fun commentLiked(
        @Field("_id") _id: Int,
        @Field("op") op: Int
    ): BaseCode

    /**
     * 评论回复点赞
     */
    @FormUrlEncoded
    @POST("/comment/reply/liked/")
    suspend fun commentReplyLiked(
        @Field("_id") _id: Int,
        @Field("op") op: Int
    ): BaseCode

    /**
     * 评论删除
     */
    @FormUrlEncoded
    @POST("/comment/del/")
    suspend fun commentDel(@Field("id") id: Int): BaseCode

    /**
     * 设置精华
     */
    @FormUrlEncoded
    @POST("/comment/good/")
    suspend fun commentGood(@Field("id") id: Int): BaseCode

    /**
     * 置顶
     */
    @FormUrlEncoded
    @POST("/comment/top/")
    suspend fun commentTop(@Field("id") id: Int): BaseCode

    /**
     * 书籍全部评论列表
     */
    @FormUrlEncoded
    @POST("/comment/list/")
    suspend fun commentList(
        @Field("_id") _id: Int,
        @Field("page") page: Int,
        @Field("type") type: String
    ):CommentList

    /**
     * 公开章节列表
     */
    @FormUrlEncoded
    @POST("/author/getchapter/")
    suspend fun authorGetchapter(@Field("bid") bid: Int,@Field("cid") page: Int): ChapterContent

    /**
     * 删除章节
     */
    @FormUrlEncoded
    @POST("/author/chapterdel/")
    suspend fun authorChapterDel(@Field("bid") bid: Int,@Field("cid") page: Int): BaseCode


    /**
     * 公开/收回章节
     */
    @FormUrlEncoded
    @POST("/author/chapterpublish/")
    suspend fun authorChapterPublish(@Field("bid") bid: Int,@Field("cid") page: Int): BaseCode


    /**
     * 上移
     */
    @FormUrlEncoded
    @POST("/author/chaptermoveup/")
    suspend fun authorChaptermoveup(@Field("bid") bid: Int,
                                    @Field("cid") page: Int,
                                    @Field("order") order: Int): BaseCode

    /**
     * 下移
     */
    @FormUrlEncoded
    @POST("/author/chaptermovedown/")
    suspend fun authorChaptermovedown(@Field("bid") bid: Int,@Field("cid") page: Int,
                                      @Field("order") order: Int): BaseCode

    @FormUrlEncoded
    @POST("/author/chaptertiming/")
    suspend fun authorChaptertiming(@Field("bid") bid: Int,@Field("cid") cid: Int,
                                    @Field("timing") timing: String): BaseCode


    /**
     * 首页所有数据
     */
    @POST("/data/base/")
    suspend fun dataBase(): DataBase


    /**
     * 数据情况
     */
    @FormUrlEncoded
    @POST("/data/survey/")
    suspend fun dataSurvey(@Field("bid") bid: Int): DataSurvey

    /**
     * 订阅情况
     */
    @FormUrlEncoded
    @POST("/data/subscribe/")
    suspend fun dataSubscribe(@Field("bid") bid: Int): DataSub


    /**
     * 订阅详情
     */
    @FormUrlEncoded
    @POST("/data/subscribe/detail/")
    suspend fun dataSubscribeDetail(@Field("bid") bid: Int,@Field("page") page: Int): DataSubscribeDetail

    /**
     * 礼物概况
     */
    @FormUrlEncoded
    @POST("/data/gift/")
    suspend fun dataGift(@Field("bid") bid: Int): DataGift

    /**
     * 稿费构成
     */
    @FormUrlEncoded
    @POST("/data/income/")
    suspend fun dataIncome(@Field("bid") bid: Int): DataIncome

    /**
     * 稿费构成
     */
    @FormUrlEncoded
    @POST("/data/payment/")
    suspend fun dataPayment(@Field("bid") bid: Int): Payment







    /**
     * 手机验证码登录
     */

    @FormUrlEncoded
    @POST("/login/code/")
    suspend fun loginCode(
        @Field("country") country: String,
        @Field("phone") phone: String,
        @Field("code") code: String
    ): LoginInfo

    /**
     * 请求国别码
     */
    @GET("/register/ip2country/")
    suspend fun ip2country(): CountryEntity

    /**
     * 发送验证码
     */

    @FormUrlEncoded
    @POST("/register/sendcode/")
    suspend fun registerSendcode(
        @Field("country") country: String,
        @Field("phone") phone: String
    ): BaseCode

    @FormUrlEncoded
    @POST("/register/sendbindemail/")
    suspend fun sendbindemail(@Field("email") email: String): BaseCode

    @FormUrlEncoded
    @POST("/login/sendvoice/")
    suspend fun sendvoice(@Field("country") country: String,
                            @Field("phone") phone: String): BaseCode


    /**
     * 手机号一键登录
     */
    @FormUrlEncoded
    @POST("/register/onekey/")
    suspend fun registerOnekey(@Field("tk") tk: String): LoginInfo


    /**
     * 发送验证码
     */

    @FormUrlEncoded
    @POST("/login/sendcode/")
    suspend fun loginSendcode(
        @Field("country") country: String,
        @Field("phone") phone: String
    ): BaseCode


    /**
     * 发送语音验证码-注册-绑定
     */

    @FormUrlEncoded
    @POST("/register/sendvoice/")
    suspend fun registerSendvoice(
        @Field("country") country: String,
        @Field("phone") phone: String
    ): BaseCode


    /**
     * 发送邮箱验证码
     */
    @FormUrlEncoded
    @POST("/register/sendemail/")
    suspend fun registerSendemail(@Field("email") email: String):BaseCode

    /**
     * 邮箱注册
     */
    @FormUrlEncoded
    @POST("/register/email/")
    suspend fun registerEmail(
        @Field("email") email: String,
        @Field("code") code: String,
        @Field("password") password: String
    ):LoginInfo

    @FormUrlEncoded
    @POST("/login/editpass/email/")
    suspend fun editpassEmail(
        @Field("email") email: String,
        @Field("code") code: String,
        @Field("password") password: String,
        @Field("repassword") nick: String
    ):LoginInfo

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("/register/signup/")
    suspend fun register(
        @Field("country") country: String,
        @Field("phone") phone: String,
        @Field("code") code: String,
        @Field("password") password: String
    ): LoginInfo

    /**
     * 修改密码并登陆
     */

    @FormUrlEncoded
    @POST("/login/editpass/")
    suspend fun editpass(
        @Field("country") country: String,
        @Field("phone") phone: String,
        @Field("code") code: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): LoginInfo

    /**
     * 意见反馈
     */
    @FormUrlEncoded
    @POST("/feedback/add/")
    suspend fun feedbackAdd(
        @Field("content") content: String,
        @Field("urls") urls: String
    ): FeedBackAdd

    /**
     * 意见反馈
     */
    @Multipart
    @POST("/feedback/upimg/")
    suspend fun feedbackUpimg(@Part file: MultipartBody.Part): UploadAvatarFile


    /**
     * 绑定手机号
     */
    @FormUrlEncoded
    @POST("/register/bind/")
    suspend fun registerBind(
        @Field("country") country: String,
        @Field("phone") phone: String,
        @Field("code") code: String
    ): BaseCode

    /**
     * 绑定邮箱
     */
    @FormUrlEncoded
    @POST("/register/bindemail/")
    suspend fun registerBindemail(
        @Field("email") email: String,
        @Field("code") code: String
    ): BaseCode

    /**
     * 修改密码
     */
    @FormUrlEncoded
    @POST("/me/editpass")
    suspend fun meEditpass(
        @Field("oldpass") oldpass: String,
        @Field("newpass") newpass: String
    ):BaseCode

    /**
     * 退出登录
     */
    @POST("/logout/")
    suspend fun logout(): BaseCode

    /**
     * 屏蔽列表
     */
    @FormUrlEncoded
    @POST("/flow/blacklist/")
    suspend fun flowBlacklist(@Field("page")  page:Int): FlowBlack

    /**
     * 取消屏蔽
     */
    @FormUrlEncoded
    @POST("/flow/removeblacklist/")
    suspend fun flowRemoveblacklist(@Field("did")  did:Int): BaseCode

    /**
     * 添加屏蔽
     */
    @FormUrlEncoded
    @POST("/flow/addblacklist/")
    suspend fun flowAddblacklist(@Field("did")  did:Int):BaseCode

}