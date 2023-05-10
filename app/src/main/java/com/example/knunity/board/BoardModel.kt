package com.example.knunity.board

import java.io.Serializable


data class BoardModel (
    val what:String="",
    val key: String="",
    var uid: String="",
    val title: String="",
    val contents: String="",
    val time : String="",
    val nick : String=""
//    var likCount : String="",
//    var comCount : String="",
//    var scrCount : String=""
): Serializable