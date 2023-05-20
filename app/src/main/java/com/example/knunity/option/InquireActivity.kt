package com.example.knunity.option

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.knunity.MainActivity
import com.example.knunity.databinding.ActivityInquireBinding
import com.example.knunity.databinding.ActivitySecretWriteBinding
import com.example.knunity.secret.SecretBoardModel
import com.example.knunity.utils.FBAuth
import com.example.knunity.utils.FBRef
import com.example.knunity.utils.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class InquireActivity : AppCompatActivity() {
    private val binding: ActivityInquireBinding by lazy {
        ActivityInquireBinding.inflate(layoutInflater)
    }
    private lateinit var nick : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        FBRef.userRef.child(FBAuth.getUid()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userModel = snapshot.getValue(UserModel::class.java)
                nick = userModel?.nickname.toString() // 가져온 닉네임 정보

                // 가져온 닉네임 정보를 사용하여 필요한 작업 수행
            }

            override fun onCancelled(error: DatabaseError) {
                // 에러 발생시 처리
                Log.w("FirebaseTest", "Failed to read value.", error.toException())
            }
        })

        write()
        onBackPressed()
    }


    private fun write() {
        binding.writebtn.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            Toast.makeText(this,"문의가 접수되었습니다.",Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        binding.writeBack.setOnClickListener {
            //startActivity(Intent(this, BoardListActivity::class.java))
            finish()
        }
    }
}