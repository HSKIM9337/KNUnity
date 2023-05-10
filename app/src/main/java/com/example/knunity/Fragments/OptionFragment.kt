package com.example.knunity.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.knunity.databinding.FragmentOptionBinding


class OptionFragment : Fragment() {
    private var _binding : FragmentOptionBinding? = null
    private val binding : FragmentOptionBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOptionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding=null
        super.onDestroyView()
    }
}