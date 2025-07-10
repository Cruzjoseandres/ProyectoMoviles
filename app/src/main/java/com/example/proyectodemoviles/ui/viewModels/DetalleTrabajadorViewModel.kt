package com.example.proyectodemoviles.ui.viewModels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.proyectodemoviles.models.Trabajadores
import kotlinx.coroutines.launch

class DetalleTrabajadorViewModel(private val application: Application): AndroidViewModel(application)  {
    private val _trabajador: MutableLiveData<Trabajadores> = MutableLiveData()
    val trabajador: LiveData<Trabajadores> = _trabajador

    private val _error: MutableLiveData<String?> = MutableLiveData()
    val error: LiveData<String?> = _error

    fun loadTrabajador(id: Int) {
        viewModelScope.launch {
            try {
                val sharedPrefs = application.getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
                val token = sharedPrefs.getString("AUTH_TOKEN", null)

                if (token != null) {
                    val trabajador = TrabajadoresRepository.getTrabajadorById(id, token)
                    _trabajador.postValue(trabajador)
                    _error.postValue(null)
                } else {
                    _error.postValue("No hay token de autenticación. Por favor, inicie sesión primero.")
                    Log.e("DetalleTrabajadorViewModel", "Token de autenticación no encontrado")
                }
            } catch (e: Exception) {
                _error.postValue("Error al cargar los trabajadores: ${e.message}")
                Log.e("DetalleTrabajadorViewModel", "Error al cargar datos de trabajador", e)
            }
        }
    }
}