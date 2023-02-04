package com.example.knunity.comment

import java.io.Serializable

data class MycommentModel(
    val key: String="",
    val userUid: String="",
    val title: String="",
    val contents: String="",
    val time : String=""
): Serializable