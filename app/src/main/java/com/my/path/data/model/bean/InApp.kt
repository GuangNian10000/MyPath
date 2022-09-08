package com.my.path.data.model.bean

/**
 * @author GuangNian
 * @description:
 * @date : 2022/8/12 4:08 下午
 */
data class InApp(val type:Int=0,val bookId:Int,val uid:Int=0,val hid:Int=0){

}

fun inBookHome(bookId:Int=0) = InApp(3,bookId,0,0)

fun inUserHome(uid:Int=0) = InApp(1,0,uid,0)

fun inRedHome(hid:Int=0) = InApp(2,0,0,hid)