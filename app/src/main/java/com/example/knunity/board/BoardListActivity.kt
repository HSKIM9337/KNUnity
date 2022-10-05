package com.example.knunity.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.knunity.databinding.ActivityBoardListBinding
import com.google.firebase.auth.FirebaseAuth

class BoardListActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private val binding : ActivityBoardListBinding by lazy {
        ActivityBoardListBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }
}