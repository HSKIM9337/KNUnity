package com.example.knunity.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {
    companion object {
        private val database = Firebase.database

        val boardRef = database.getReference("board")

        val commentRef = database.getReference("comment")

        val likeboardRef = database.getReference("likeboard")

        val scrapboardRef = database.getReference("scrap")
        val likeRef = database.getReference("like")
       //val addlikeRef= database.getReference("likeboard").child()
    }
}