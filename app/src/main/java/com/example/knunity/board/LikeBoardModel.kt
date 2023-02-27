package com.example.knunity.board

import java.io.Serializable

data class LikeBoardModel(
    val key: String="",
    val userUid: String="",
    val title: String="",
    val contents: String="",
    val time : String=""
): Serializable


