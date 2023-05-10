<<<<<<<< HEAD:app/src/main/java/com/example/knunity/comment/CommentBoardModel.kt
package com.example.knunity.comment

import java.io.Serializable

data class CommentBoardModel(
========
package com.example.knunity.board

import java.io.Serializable

data class DeclarationBoardModel(
>>>>>>>> origin/jwptest:app/src/main/java/com/example/knunity/board/DeclarationBoardModel.kt
    val key: String="",
    val userUid: String="",
    val title: String="",
    val contents: String="",
    val time : String=""
): Serializable
