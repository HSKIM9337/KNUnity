package com.example.knunity.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.knunity.board.BoardListActivity
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

        move_to()

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun move_to() {
        binding.WriteFreeET.setOnClickListener {
            val intent = Intent(context,BoardListActivity::class.java)
            startActivity(intent)
        }
    }
}