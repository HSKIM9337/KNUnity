package com.example.knunity.comment

import java.io.Serializable

data class CommentModel (
    val commentTitle : String="",
    val commentCreatedTime : String="",
    var commenteryUid : String="",
    val boardKeyda : String="",
    val currentkey: String="",
    val uid:String=""
   // var nick : String=""
):Serializable