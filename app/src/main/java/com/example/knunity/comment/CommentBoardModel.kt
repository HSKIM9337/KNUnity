package com.example.knunity.comment

import java.io.Serializable

data class CommentBoardModel(
    val key: String="",
    val userUid: String="",
    val title: String="",
    val contents: String="",
    val time : String=""
): Serializable