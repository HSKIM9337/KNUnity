package com.example.knunity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.knunity.Fragments.BoardFragment
import com.example.knunity.Fragments.HomeFragment
import com.example.knunity.Fragments.OptionFragment
import com.example.knunity.Fragments.UniqueFragment
import com.example.knunity.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth=Firebase.auth
//        binding.logoutBtn.setOnClickListener {
//            auth.signOut()
//            finish()
//
//        }
        setupBottomNavigationView()
    }

    private fun setupBottomNavigationView() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragment_Home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, HomeFragment())
                        .commit()
                    true
                }
                R.id.fragment_Board -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, BoardFragment())
                        .commit()
                    true
                }
                R.id.fragment_Unique -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, UniqueFragment())
                        .commit()
                    true
                }
                R.id.fragment_Option -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, OptionFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }


}