package com.example.proyectodemoviles.ui.viewModels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.proyectodemoviles.models.User
import com.example.proyectodemoviles.repositories.UsuarioReporitory
import kotlinx.coroutines.launch

class HomeViewModel(private val application: Application): AndroidViewModel(application) {
    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User> = _user

    private val _error: MutableLiveData<String?> = MutableLiveData()
    val error: LiveData<String?> = _error

    fun getUser() {
        viewModelScope.launch {
            try {
                val sharedPrefs = application.getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
                val token = sharedPrefs.getString("AUTH_TOKEN", null)
                if (token != null) {
                    val userInSession = UsuarioReporitory.getUserInsession(token)
                    _user.postValue(userInSession)
                    _error.postValue(null)
                } else {
                    _error.postValue("No hay token de autenticación. Por favor, inicie sesión primero.")
                    Log.e("HomeViewModel", "Token de autenticación no encontrado")
                }
            } catch (e: Exception) {
                _error.postValue("Error al cargar datos de usuario  ${e.message}")
                Log.e("HomeViewModel", "Error al cargar datos de usuario", e)
            }
        }
    }


}