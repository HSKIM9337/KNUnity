package com.example.knunity.job

import java.io.Serializable

data class JobModel(
    val key: String="",
    var uid: String="",
    val title: String="",
    val contents: String="",
    val time : String=""
): Serializable
