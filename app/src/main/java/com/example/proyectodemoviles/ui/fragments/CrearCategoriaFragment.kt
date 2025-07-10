package com.example.proyectodemoviles.ui.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proyectodemoviles.databinding.FragmentCrearCategoriaBinding
import com.example.proyectodemoviles.ui.viewModels.CrearCategoriaViewModel

class CrearCategoriaFragment : Fragment() {
    private lateinit var binding: FragmentCrearCategoriaBinding
    private val viewModel: CrearCategoriaViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCrearCategoriaBinding.inflate(inflater, container, false)
        return binding.root
    }
}