package com.example.knunity.hotboard

import java.io.Serializable

data class LikeBoardModel(
    val key: String="",
    val userUid: String="",
    val title: String="",
    val contents: String="",
    val time : String=""
   // val likeUser : MutableMap<String, Boolean> = HashMap()
    //val userUid: String="",
    //var liking:Boolean=false
    //val boardKeydari:String=""
    //val likelisting: ArrayList<String>
): Serializable


