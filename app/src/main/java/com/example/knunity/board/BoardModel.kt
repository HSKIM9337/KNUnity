package com.example.knunity.board

import java.io.Serializable


data class BoardModel (
    val key: String="",
    val uid: String="",
    val title: String="",
    val contents: String="",
    val time : String=""
): Serializable