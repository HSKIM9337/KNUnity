package com.example.knunity.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.knunity.board.*
import com.example.knunity.databinding.FragmentBoardBinding


class BoardFragment : Fragment() {
    private var _binding : FragmentBoardBinding? = null
    private val binding : FragmentBoardBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBoardBinding.inflate(inflater,container,false)
        my_to()
        move_to()
        comment_to()
        scrap_to()
        like_to()
        job_to()
        secret_to()
        incruit_to()
       // favorite()
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    private fun favorite()
    {
        val free = binding.linearLayout3.getChildAt(0)

    }
    private fun my_to() {
        binding.MyBoardET.setOnClickListener {
            val intent = Intent(context,BoardMyListActivity::class.java)
            startActivity(intent)
        }
    }
    private fun move_to() {
        binding.WriteFreeET.setOnClickListener {
            val intent = Intent(context,BoardListActivity::class.java)
            startActivity(intent)
        }
    }
    private fun comment_to(){
        binding.CommentListET.setOnClickListener{
            val intent = Intent(context,BoardMycommentListActivity::class.java)
            startActivity(intent)
        }
    }
    private fun scrap_to() {
        binding.ScrapListET.setOnClickListener {
            val intent = Intent(context,BoradScrapListActivity::class.java)
            startActivity(intent)
        }
    }
    private fun like_to() {
        binding.LikeListET.setOnClickListener {
            val intent = Intent(context,BoardLikeListActivity::class.java)
            startActivity(intent)
        }
    }
    private fun job_to()
    {
        binding.JobET.setOnClickListener {
            val intent=Intent(context,IncruitListActivity::class.java)
            startActivity(intent)
        }
    }
    private fun secret_to()
    {
        binding.SecretET.setOnClickListener {
            val intent = Intent(context,BoardSecretListActivity::class.java)
            startActivity(intent)
        }
    }
    private fun incruit_to()
    {
        binding.incruitET.setOnClickListener{
            val intent = Intent(context,JobListActivity::class.java)
            startActivity(intent)
        }
    }
}