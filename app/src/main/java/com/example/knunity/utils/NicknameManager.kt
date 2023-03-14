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
//fun createNickname(nickname: String, userId: String, callback: (Boolean) -> Unit) {
//    FBRef.nickRef.child(nickname).addListenerForSingleValueEvent(object : ValueEventListener {
//        override fun onDataChange(dataSnapshot: DataSnapshot) {
//            if (dataSnapshot.exists()) {
//                callback(false)
//            } else {
//                // check if nickname is already used by another user
//                FBRef.userRef.orderByChild("nickname").equalTo(nickname).addListenerForSingleValueEvent(object : ValueEventListener {
//                    override fun onDataChange(dataSnapshot: DataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            for (userSnapshot in dataSnapshot.children) {
//                                if (userSnapshot.key != userId) {
//                                    callback(false)
//                                    return
//                                }
//                            }
//                        }
//                        FBRef.nickRef.child(nickname).setValue(userId)
//                        callback(true)
//                    }
//
//                    override fun onCancelled(databaseError: DatabaseError) {
//                        callback(false)
//                    }
//                })
//            }
//        }
//
//        override fun onCancelled(databaseError: DatabaseError) {
//            callback(false)
//        }
//    })
//}
//
//    fun checkNicknameExists(nickname: String, callback: (Boolean) -> Unit) {
//        FBRef.nickRef.child(nickname).addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                callback(dataSnapshot.exists())
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                callback(false)
//            }
//        })
//    }
}