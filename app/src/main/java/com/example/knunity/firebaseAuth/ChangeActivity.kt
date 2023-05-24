package com.example.knunity.firebaseAuth

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.knunity.MainActivity
import com.example.knunity.databinding.ActivityChangeBinding
import com.example.knunity.databinding.ActivityJoinBinding
import com.example.knunity.utils.FBAuth
import com.example.knunity.utils.FBRef
import com.example.knunity.utils.NicknameManager
import com.example.knunity.utils.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class ChangeActivity : AppCompatActivity() {
       // private lateinit var auth: FirebaseAuth

    private val binding: ActivityChangeBinding by lazy {
            ActivityChangeBinding.inflate(layoutInflater)
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(binding.root)

            binding.nicknameBtn.setOnClickListener {
                val nickname = binding.nicknameArea.text.toString()
                if (nickname.isEmpty()) {
                    Toast.makeText(this, "닉네임을 입력하세요", Toast.LENGTH_SHORT).show()
                } else {
                    NicknameManager.checkNicknameExists(nickname) { nicknameExists ->
                        if (nicknameExists) {
                            binding.nicknameArea.setText("")
                            Toast.makeText(this, "이미 사용 중인 닉네임입니다", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "사용 가능한 닉네임입니다", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            //auth = Firebase.auth
            binding.lastCheckBtn.setOnClickListener() {
                var isGoToJoin = true
                val nick = binding.nicknameArea.text.toString()
//                val password1 = binding.passwordArea1.text.toString()
//                val password2 = binding.passwordArea2.text.toString()


//                if (email.isEmpty()) {
//                    Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
//                    isGoToJoin = false
//                }
//                if (password1.isEmpty()) {
//                    Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
//                    isGoToJoin = false
//                }
                if(nick.isEmpty())
                {
                    Toast.makeText(this, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
                    isGoToJoin = false
                }
//                if (password2.isEmpty()) {
//                    Toast.makeText(this, "비밀번호 확인을 입력해주세요", Toast.LENGTH_SHORT).show()
//                    isGoToJoin = false
//                }
//                if (password1 != password2) {
//                    Toast.makeText(this, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
//                    isGoToJoin = false
//                }
//
////            if (!email.endsWith("@knu.ac.kr")) {
////                Toast.makeText(this, "경북대학교 웹 메일만 사용 가능합니다", Toast.LENGTH_SHORT).show()
////                isGoToJoin = false
////            }
//
//                if (password1.length < 6) {
//                    Toast.makeText(this, "비밀번호는 여섯자 이상 입력해주세요", Toast.LENGTH_SHORT).show()
//                    isGoToJoin = false
//                }
//
                NicknameManager.createNicknameWithCheck(binding.nicknameArea.text.toString()) { nicknameCreated ->
                    if (nicknameCreated) {
                        // 닉네임 생성 성공
                        isGoToJoin=true
                        //  FBRef.userRef.child(FBAuth.getUid()).setValue(UserModel(FBAuth.getUid(), binding.nicknameArea.text.toString()))
                        Toast.makeText(this, "닉네임 생성 성공", Toast.LENGTH_SHORT).show()
                    } else {
                        isGoToJoin=false
                        // 닉네임 생성 실패
                        //Toast.makeText(this, "이미 사용 중인 닉네임입니다", Toast.LENGTH_SHORT).show()
                    }
                }
                if (isGoToJoin) {
//                    auth.createUserWithEmailAndPassword(email, password1)
//                        .addOnCompleteListener(this) { task ->
//                            if (task.isSuccessful) {
//                                Toast.makeText(this, "성공", Toast.LENGTH_SHORT).show()
                                FBRef.userRef.child(FBAuth.getUid()).setValue(UserModel(FBAuth.getUid(), binding.nicknameArea.text.toString()))
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()

                            } else {
                                Toast.makeText(this, "실패", Toast.LENGTH_SHORT).show()
                            }
                        }
                }


            }

