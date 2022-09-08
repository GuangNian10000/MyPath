package com.my.path.data.model.bean

/**
 * @author GuangNian
 * @description:
 * @date : 2022/2/25 11:43 上午
 */
data class TipTicket(
    val `data`: ArrayList<TipTicketData> = ArrayList(),
    val st: Int=0
)

data class TipTicketData(
    var bookName:String = "",
    val addtime: String="",
    val num: Int=0,
    val uinfo: Uinfo = Uinfo()
)
