package com.example.proyectodemoviles.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.proyectodemoviles.R
import com.example.proyectodemoviles.databinding.FragmentRegisterBinding
import com.example.proyectodemoviles.models.User
import com.example.proyectodemoviles.ui.viewModels.RegisterViewModel

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        setupEventListeners()
        setupEventObservers()
        return binding.root
    }

    private fun setupEventObservers() {
        viewModel.nameError.observe(viewLifecycleOwner) { error ->
            binding.nameEditText.error = error
        }

        viewModel.lastNameError.observe(viewLifecycleOwner) { error ->
            binding.lastNameEditText.error = error
        }

        viewModel.emailError.observe(viewLifecycleOwner) { error ->
            binding.emailEditText.error = error
        }

        viewModel.passwordError.observe(viewLifecycleOwner) { error ->
            binding.passwordEditText.error = error
        }

        viewModel.registro.observe(viewLifecycleOwner) { mensaje ->
            if (mensaje != null) {
                Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
                if (mensaje.startsWith("Registro exitoso")) {
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
            }
        }
    }

    private fun setupEventListeners() {
        binding.registerButton.setOnClickListener {
            if (validateInputs()) {
                registerUser()
            }
        }

        binding.loginLinkTextView.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }



    private fun validateInputs(): Boolean {
        val name = binding.nameEditText.text.toString().trim()
        val lastName = binding.lastNameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        return viewModel.validateEmail(name, lastName, email, password)
    }

    private fun registerUser() {
       val registroUser = User(
            name = binding.nameEditText.text.toString().trim(),
            lastName = binding.lastNameEditText.text.toString().trim(),
            email = binding.emailEditText.text.toString().trim(),
            password = binding.passwordEditText.text.toString().trim(),
            profile = null
       )
        viewModel.registerUser(registroUser)
    }


}
