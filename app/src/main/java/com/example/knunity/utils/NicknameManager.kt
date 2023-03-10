package com.example.knunity.utils

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

object NicknameManager
{
    fun createNickname(nickname: String, callback: (Boolean) -> Unit) {
    FBRef.nickRef.child(nickname).addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
                callback(false)
            } else {
                FBRef.nickRef.child(nickname).setValue(true)
                callback(true)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            callback(false)
        }
    })
}

fun checkNicknameExists(nickname: String, callback: (Boolean) -> Unit) {
    FBRef.nickRef.child(nickname).addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            callback(dataSnapshot.exists())
        }

        override fun onCancelled(databaseError: DatabaseError) {
            callback(false)
        }
    })
}
}