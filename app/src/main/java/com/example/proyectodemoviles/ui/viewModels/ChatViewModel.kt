package com.example.proyectodemoviles.ui.viewModels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectodemoviles.models.ListAppointments
import com.example.proyectodemoviles.models.TrabajadoresList
import com.example.proyectodemoviles.repositories.AppointmentsRepository
import kotlinx.coroutines.launch

class ChatViewModel(private val application: Application): AndroidViewModel(application) {
    private val _chats: MutableLiveData<ListAppointments> = MutableLiveData(arrayListOf())
    val chats: LiveData<ListAppointments> = _chats

    private val _error: MutableLiveData<String?> = MutableLiveData()
    val error: LiveData<String?> = _error

    fun loadChatsUser() {
        viewModelScope.launch {
            try {
                val sharedPrefs = application.getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
                val token = sharedPrefs.getString("AUTH_TOKEN", null)

                if (token != null) {
                    val chatsList = AppointmentsRepository.getAppointments(token)
                    _chats.postValue(chatsList)
                    _error.postValue(null)
                } else {
                    _error.postValue("No hay token de autenticación. Por favor, inicie sesión primero.")
                    Log.e("ChatViewModel", "Token de autenticación no encontrado")
                }
            } catch (e: Exception) {
                _error.postValue("Error al cargar los chats: ${e.message}")
                Log.e("ChatViewModel", "Error loading chats", e)
            }
        }
    }

}