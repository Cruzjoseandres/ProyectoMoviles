package com.example.proyectodemoviles.api

import com.example.proyectodemoviles.models.ListAppointments
import com.example.proyectodemoviles.models.User
import com.example.proyectodemoviles.models.ListCategory
import com.example.proyectodemoviles.models.LoginRequest
import com.example.proyectodemoviles.models.LoginResponse
import com.example.proyectodemoviles.models.RegisterResponse
import com.example.proyectodemoviles.models.Trabajadores
import com.example.proyectodemoviles.models.TrabajadoresList
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface JSONPlaceHolderApi {
    @POST("client/register")
    suspend fun registerUser(@Body user: User): Response<RegisterResponse>

    @POST("client/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("categories")
    suspend fun getCategories(
        @Header("Authorization") token: String
    ): ListCategory

    @GET("categories/{categoryId}/workers")
    suspend fun getWorkersByCategory(
        @Path("categoryId") categoryId: Int,
        @Header("Authorization") token: String
    ): TrabajadoresList


    @GET("workers/{id}")
    suspend fun getTrabajadorId(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Trabajadores


    @GET("me")
    suspend fun getUserInsessionId(
        @Header("Authorization") token: String
    ): User

    @GET("appointments")
    suspend fun getAppointments(
        @Header("Authorization") token: String
    ): ListAppointments
}