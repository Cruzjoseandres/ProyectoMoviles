package com.example.proyectodemoviles.ui.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.proyectodemoviles.R
import com.example.proyectodemoviles.databinding.FragmentHomeBinding
import com.example.proyectodemoviles.ui.viewModels.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupEventListeners()
        setupObservers()
        viewModel.getUser()
        return binding.root
    }

    private fun setupEventListeners() {
        binding.btnChats.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToChatFragment()
            findNavController().navigate(action)
        }

        binding.btnCategorias.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchCategoriesFragment()
            findNavController().navigate(action)
        }
    }

    private fun setupObservers() {
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.tvBienvenido.text = "Bienvenido ${user.name} ${user.profile?.lastName}"
        }
    }
}