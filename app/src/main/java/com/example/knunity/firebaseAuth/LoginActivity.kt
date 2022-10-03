package com.example.knunity.firebaseAuth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.knunity.MainActivity
import com.example.knunity.R
import com.example.knunity.databinding.ActivityIntroBinding
import com.example.knunity.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val binding : ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = Firebase.auth

        login()
    }

    //로그인 하기
    private fun login() {
        binding.loginBtn.setOnClickListener {

            var email = binding.emailArea.text.toString()
            var password = binding.passwordArea.text.toString()


            //파이어베이스에서 이메일정보랑 패스와드 가져와서 일치하는지 확인하기
            //참고 https://firebase.google.com/docs/auth/android/start?hl=ko#kotlin+ktx_1


            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "이메일이나 비밀번호가 올바른지 확인해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }


        }
    }
}