package com.example.knunity.Incruit

import java.io.Serializable

data class IncruitModel(
    val key: String="",
    var uid: String="",
    val title: String="",
    val contents: String="",
    val time : String=""
): Serializable
