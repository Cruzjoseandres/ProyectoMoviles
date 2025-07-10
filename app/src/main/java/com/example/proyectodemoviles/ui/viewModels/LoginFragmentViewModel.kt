package com.example.proyectodemoviles.ui.viewModels

import android.app.Application
import android.content.Context
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.proyectodemoviles.models.User
import com.example.proyectodemoviles.repositories.UsuarioReporitory
import kotlinx.coroutines.launch

class LoginFragmentViewModel(private val application: Application): AndroidViewModel(application) {
    private val _emailError : MutableLiveData<String?> = MutableLiveData()
    val emailError: MutableLiveData<String?> = _emailError

    private val _passwordError : MutableLiveData<String?> = MutableLiveData()
    val passwordError: MutableLiveData<String?> = _passwordError

    private val _login : MutableLiveData<String> = MutableLiveData()
    val login: MutableLiveData<String> = _login


    fun validateImputs(email: String, password: String): Boolean {
        var isValid = true

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

    fun Login(usuario : User) {
        viewModelScope.launch {
            try {
                val loginResponse = UsuarioReporitory.loginUser(usuario)

                val sharedPrefs = application.getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
                sharedPrefs.edit().putString("AUTH_TOKEN", loginResponse.accessToken).apply()

                _login.postValue("Login exitoso")
            } catch (e: Exception) {
                _login.postValue("Error al iniciar sesión: ${e.message}")
            }
        }
    }
}