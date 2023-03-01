package com.example.knunity.board

import java.io.Serializable

data class ScrapModel(
    val what: String="",
    val key: String="",
    val userUid: String="",
    val title: String="",
    val contents: String="",
    val time : String=""
   // val userUid: String=""

): Serializable
