package com.example.proyectodemoviles.ui.viewModels

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectodemoviles.models.User
import com.example.proyectodemoviles.repositories.UsuarioReporitory
import kotlinx.coroutines.launch

class RegisterViewModel: ViewModel() {
    private val _nameError : MutableLiveData<String?> = MutableLiveData()
    val nameError: MutableLiveData<String?> = _nameError

    private val _lastNameError : MutableLiveData<String?> = MutableLiveData()
    val lastNameError: MutableLiveData<String?> = _lastNameError

    private val _emailError : MutableLiveData<String?> = MutableLiveData()
    val emailError: MutableLiveData<String?> = _emailError

    private val _passwordError : MutableLiveData<String?> = MutableLiveData()
    val passwordError: MutableLiveData<String?> = _passwordError

    private val _registro : MutableLiveData<String> = MutableLiveData()
    val registro: MutableLiveData<String> = _registro


    fun validateEmail(name:String, lastName: String, email: String, password: String): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            _nameError.postValue("El nombre es requerido")
            isValid = false
        }else {
            _nameError.value = null
        }

        if (lastName.isEmpty()) {
            _lastNameError.postValue("El apellido es requerido")
            isValid = false
        }else {
            _lastNameError.value = null
        }

        if (email.isEmpty()) {
            _emailError.value = "El email no puede estar vacío"
            isValid = false
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailError.value = "Email inválido"
            isValid = false
        } else {
            _emailError.value = null
        }

        if (password.isEmpty()) {
            _passwordError.value = "La contraseña no puede estar vacía"
            isValid = false
        }else if (password.length < 6) {
            _passwordError.value = "La contraseña debe tener al menos 6 caracteres"
            isValid = false
        } else {
            _passwordError.value = null
        }
        return isValid

    }

    fun registerUser(usuario : User) {
        viewModelScope.launch {
            try {
                val registerResponse = UsuarioReporitory.registerUser(usuario)
                _registro.postValue("Registro exitoso: ${registerResponse.name}")
            }catch (e: Exception) {
                _registro.postValue("Registro Fallido: ${e.message}")
            }
        }
    }
}