package com.example.knunity.firebaseAuth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.knunity.R
import com.example.knunity.databinding.ActivityIntroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class IntroActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private val binding : ActivityIntroBinding by lazy {
        ActivityIntroBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth

        Login_Join()
    }

    private fun Login_Join() {
        binding.loginBtn.setOnClickListener() {

            val intent = Intent(this,LoginActivity::class.java)
            startActivity((intent))

        }

        binding.joinBtn.setOnClickListener() {
            val intent = Intent(this,JoinActivity::class.java)
            startActivity((intent))
        }

    }

}