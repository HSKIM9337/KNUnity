package com.example.knunity.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.knunity.R
import com.example.knunity.databinding.FragmentUniqueBinding


class UniqueFragment : Fragment() {
    private var _binding : FragmentUniqueBinding?=null
    private val binding :FragmentUniqueBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUniqueBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}