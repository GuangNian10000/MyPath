package com.my.path.app.config

/**
 * @author GuangNian
 * @description:
 * @date : 2022/2/21 10:36 上午
 */
object SysConfig {
    //空布局类型

    //写作类型
    val NUll_WRItING_LIST = "NUll_WRItING_LIST"
    //主页-数据页
    val NUll_HOME_DATA = "NUll_HOME_DATA"
    //章节类型
    val NUll_CHAPTER_LIST = "NUll_CHAPTER_LIST"
    //日历
    val NUll_RIli_LIST = "NUll_RIli_LIST"

    val NUll_MESSAGE_LIST = "NUll_MESSAGE_LIST"

    @kotlin.jvm.JvmField
    val BookRounded = 12

    @kotlin.jvm.JvmField
    val YINSI = "YINSI"
    @kotlin.jvm.JvmField
    val FWUXIY = "FWUXIY"
    @kotlin.jvm.JvmField
    val CZXIY = "CZXIY"

    @kotlin.jvm.JvmField
    val yinSiUrl = "/privacy/"  //隐私政策
    @kotlin.jvm.JvmField
    val serviceAgreementUrl="/agreement/" //用户服务协议
    @kotlin.jvm.JvmField
    val topUptUrl="/apay/agreement/"    //充值协议

    @kotlin.jvm.JvmField
    val page = "com.sdt.dlxk.fileprovider"

    //帮助中心
    @JvmStatic
    val HELP_CENTER = "/help/"

    //阿里云一键登录密钥
    @kotlin.jvm.JvmField
    val AUTH_SECRET :String = "vWD1YwYAQ3Ly5Ruwkhyklz8irfQMzw/lXmInygd42QS4DU/OY7QNx16OmQh/1Qq+B1dCWDtpxJiWvk1f1BpXH+JahWwpNqIch0nvz7mZo/Q6sVFoJkQTRFYlopiNd35KnMe4L4PGv54KoM7/rUaaVYxuYDqmh0R+Jcx6LTTsQGexDHYpXe0/Ur3+xaPvM4IcOW1WvXjjasQonqht4XUdNqmzw4IeugILj21HAZSkS+WWIqJkcTR7RuJhlh4EtMhRn/GZYwG5hEe6FstvmrhIaODkGgVPv3sx1VjeggrhjoE="

}