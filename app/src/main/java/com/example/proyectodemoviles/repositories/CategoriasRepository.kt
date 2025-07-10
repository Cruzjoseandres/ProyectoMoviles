package com.example.proyectodemoviles.repositories
import com.example.proyectodemoviles.models.ListCategory



object CategoriasRepository {

    suspend fun getCategorias(token: String): ListCategory {
        return RetrofitRepository
            .getJsonPlaceholderApi()
            .getCategories("Bearer $token")
    }


}