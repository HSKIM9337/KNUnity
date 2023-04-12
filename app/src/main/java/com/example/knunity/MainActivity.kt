package com.example.knunity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.knunity.Fragments.*
import com.example.knunity.board.BoardMyListActivity
import com.example.knunity.databinding.ActivityMainBinding
import com.example.knunity.firebaseAuth.IntroActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, HomeFragment())
            .commit()
//        binding.logoutBtn.setOnClickListener {
//            auth.signOut()
//            finish()
//
//        }
        binding.settingBtn.setOnClickListener{
            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            drawerLayout.openDrawer(GravityCompat.END)
        }
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_profile -> {
                    // 개인정보 화면으로 이동
                    true
                }
                R.id.menu_settings -> {
                    // 설정 화면으로 이동
                    binding.bottomNavigationView.menu.getItem(4).isChecked=true
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, OptionFragment())
                        .commit()
                    true
                }
                R.id.menu_logout -> {
                   logout() // 로그아웃 처리
                    true
                }
                else -> false
            }
        }

        setupBottomNavigationView()
    }

    private fun logout() {

            auth.signOut()
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this,"로그아웃하였습니다.",Toast.LENGTH_SHORT).show()

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
                R.id.fragment_Calculataor -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, CalculatorFragment())
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