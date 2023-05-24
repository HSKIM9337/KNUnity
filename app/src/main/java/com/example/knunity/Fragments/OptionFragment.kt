package com.example.knunity.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.knunity.databinding.FragmentOptionBinding
import com.example.knunity.firebaseAuth.ChangeActivity
import com.example.knunity.firebaseAuth.IntroActivity
import com.example.knunity.option.InquireActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class OptionFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var _binding : FragmentOptionBinding? = null
    private val binding : FragmentOptionBinding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOptionBinding.inflate(inflater,container,false)
        auth = Firebase.auth
      nicknamechage()
        inquire()
        logout()
        return binding.root
    }

    override fun onDestroyView() {
        _binding=null
        super.onDestroyView()
    }
    private fun nicknamechage(){
        binding.nickChange.setOnClickListener {
            val intent = Intent(context, ChangeActivity::class.java)
            startActivity(intent)
        }
    }
    private fun inquire(){
        binding.inquire.setOnClickListener {
            val intent = Intent(context, InquireActivity::class.java)
            startActivity(intent)
        }
    }
    private fun logout(){
        binding.logout.setOnClickListener {
            auth.signOut()
            val intent = Intent(context, IntroActivity::class.java)
            startActivity(intent)
            Toast.makeText(context,"로그아웃하였습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}