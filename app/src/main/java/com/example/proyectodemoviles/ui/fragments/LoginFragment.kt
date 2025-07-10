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
import com.example.proyectodemoviles.databinding.FragmentLoginBinding
import com.example.proyectodemoviles.models.User
import com.example.proyectodemoviles.ui.viewModels.LoginFragmentViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        setupEventListeners()
        setupEventObservers()
        return binding.root
    }

    private fun setupEventListeners() {

        binding.loginButton.setOnClickListener {
            if (validateInputs()) {
                login()
            }
        }

        binding.registerLinkTextView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun setupEventObservers() {
        viewModel.emailError.observe(viewLifecycleOwner) { error ->
            binding.emailEditText.error = error
        }

        viewModel.passwordError.observe(viewLifecycleOwner) { error ->
            binding.passwordEditText.error = error
        }

        viewModel.login.observe(viewLifecycleOwner) { result ->
            if (result.isNotEmpty()) {
                Toast.makeText(requireContext(), result, Toast.LENGTH_SHORT).show()
                if (result == "Login exitoso") {

                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
            }
        }
    }



    private fun validateInputs(): Boolean {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        return viewModel.validateImputs(email, password)
    }

    private fun login() {
        val usuario = User(
            name = "",
            lastName = "",
            email = binding.emailEditText.text.toString().trim(),
            password = binding.passwordEditText.text.toString().trim(),
            profile = null
        )
        viewModel.Login(usuario)
    }

}
