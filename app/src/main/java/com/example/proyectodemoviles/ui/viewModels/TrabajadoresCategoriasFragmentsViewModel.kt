package com.example.proyectodemoviles.ui.viewModels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.proyectodemoviles.models.TrabajadoresList

import kotlinx.coroutines.launch

class TrabajadoresCategoriasFragmentsViewModel (private val application: Application) : AndroidViewModel(application) {
    private val _trabajadores: MutableLiveData<TrabajadoresList> = MutableLiveData(arrayListOf())
    val trabajadores: LiveData<TrabajadoresList> = _trabajadores

    private val _error: MutableLiveData<String?> = MutableLiveData()
    val error: LiveData<String?> = _error

    fun loadTrabajadoresByCategoria(categoryId: Int) {
        viewModelScope.launch {
            try {
                val sharedPrefs = application.getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
                val token = sharedPrefs.getString("AUTH_TOKEN", null)

                if (token != null) {
                    val trabajadoresList = TrabajadoresRepository.getTrabjadoresByCategoria(categoryId, token)
                    _trabajadores.postValue(trabajadoresList)
                    _error.postValue(null)
                } else {
                    _error.postValue("No hay token de autenticación. Por favor, inicie sesión primero.")
                    Log.e("TrabajadoresViewModel", "Token de autenticación no encontrado")
                }
            } catch (e: Exception) {
                _error.postValue("Error al cargar los trabajadores: ${e.message}")
                Log.e("TrabajadoresViewModel", "Error loading trabajadores", e)
            }
        }
    }
}