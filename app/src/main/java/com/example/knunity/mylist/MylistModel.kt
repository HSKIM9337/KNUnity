package com.example.knunity.mylist

import java.io.Serializable

data class MylistModel(
    val what : String="",
    val key: String="",
    val userUid: String="",
    val title: String="",
    val contents: String="",
    val time : String="",
    val nick: String=""
): Serializable
