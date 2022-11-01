package com.example.knunity.utils

import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class FBAuth {
    companion object {
        private lateinit var auth: FirebaseAuth

        fun getUid(): String {

            auth = FirebaseAuth.getInstance()

            return auth.currentUser?.uid.toString()
        }

        fun getTime(): String {
            val currentDataeTime = Calendar.getInstance().time
            val dateFormat =
                SimpleDateFormat("yyyy-MM-dd HH:mm E", Locale.KOREA).format((currentDataeTime))

            return dateFormat
        }
    }
}