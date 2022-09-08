package com.my.path.data.model.bean

/**
 * @author GuangNian
 * @description:
 * @date : 2022/3/2 3:47 下午
 */
data class AuthorTag(
    val st: Int=0,
    val tags: ArrayList<Tag> = ArrayList()
)

data class Tag(
    val id: Int=0,
    val name: String="",
    var selected :Boolean = false
)