package com.example.knunity.secret

import java.io.Serializable

data class SecretBoardModel(
    val what : String="",
    val key: String="",
    val userUid: String="",
    val title: String="",
    val contents: String="",
    val time : String=""
): Serializable
