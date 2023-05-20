package com.example.knunity.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {
    companion object {
        private val database = Firebase.database

        val boardRef = database.getReference("board")

        val commentRef = database.getReference("comment")

        val likeboardRef = database.getReference("likeboard")
        val nickRef = database.getReference("nickname")
        val scrapboardRef = database.getReference("scrap")
        val likeRef = database.getReference("like")
        val mycommentRef = database.getReference("mycomment")
        val couserRef = database.getReference("usercheck")
        val declatioinRef = database.getReference("submit")
        val decboardRef = database.getReference("dec")
        val comboardRef = database.getReference("comb")
        val secretboardRef = database.getReference("sec")
        val jobRef = database.getReference("job")
        val incruitRef = database.getReference("incruit")
        val userRef = database.getReference("user")
        val reportRef=database.getReference("reports")
        val userreportRef=database.getReference("user_reports")
        val memoRef = database.getReference("memo")
        val myref = database.getReference("mylist")
       //val addlikeRef= database.getReference("likeboard").child()
    }
}