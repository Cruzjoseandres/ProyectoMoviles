package com.example.proyectodemoviles.ui.viewModels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.proyectodemoviles.models.ListCategory
import com.example.proyectodemoviles.repositories.CategoriasRepository
import kotlinx.coroutines.launch

class CategoryViewModel(private val application: Application) : AndroidViewModel(application) {
    private val _categories: MutableLiveData<ListCategory> = MutableLiveData(arrayListOf())
    val categories: LiveData<ListCategory> = _categories

    private val _error: MutableLiveData<String?> = MutableLiveData()
    val error: LiveData<String?> = _error


    fun loadCategories() {
        viewModelScope.launch {
            try {
                val sharedPrefs = application.getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
                val token = sharedPrefs.getString("AUTH_TOKEN", null)

                if (token != null) {
                    val categoriaList = CategoriasRepository.getCategorias(token)
                    _categories.postValue(categoriaList)
                    _error.postValue(null)
                } else {
                    _error.postValue("No hay token de autenticación. Por favor, inicie sesión primero.")
                    Log.e("CategoryViewModel", "Token de autenticación no encontrado")
                }
            } catch (e: Exception) {
                _error.postValue("Error al cargar las categorías: ${e.message}")
                Log.e("CategoryViewModel", "Error loading categories", e)
            }
        }
    }
}
