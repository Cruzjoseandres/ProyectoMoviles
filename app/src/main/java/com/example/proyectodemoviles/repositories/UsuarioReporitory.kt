package com.example.proyectodemoviles.repositories

import com.example.proyectodemoviles.models.LoginRequest
import com.example.proyectodemoviles.models.LoginResponse
import com.example.proyectodemoviles.models.RegisterResponse
import com.example.proyectodemoviles.models.User


object UsuarioReporitory {
    suspend fun registerUser(user: User): RegisterResponse {
        val response = RetrofitRepository
            .getJsonPlaceholderApi()
            .registerUser(user)

        if (!response.isSuccessful) {
            throw Exception("Error en el registro: ${response.code()} ${response.message()}")
        }

        return response.body() ?: throw Exception("Error: respuesta vacía del servidor")
    }

    suspend fun loginUser(user: User): LoginResponse {
        val loginRequest = LoginRequest(email = user.email, password = user.password)

        val response = RetrofitRepository
            .getJsonPlaceholderApi()
            .loginUser(loginRequest)

        if (!response.isSuccessful) {
            throw Exception("Error en el login: ${response.code()} ${response.message()}")
        }

        return response.body() ?: throw Exception("Error: respuesta vacía del servidor")
    }

    suspend fun getUserInsession(token: String): User {
        return RetrofitRepository
            .getJsonPlaceholderApi()
            .getUserInsessionId("Bearer $token")
    }
}