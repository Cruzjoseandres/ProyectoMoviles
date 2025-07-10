package com.example.proyectodemoviles.repositories

import com.example.proyectodemoviles.api.JSONPlaceHolderApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitRepository {
    fun getJsonPlaceholderApi(): JSONPlaceHolderApi {
        return Retrofit.Builder()
            .baseUrl("http://trabajos.jmacboy.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JSONPlaceHolderApi::class.java)
    }
}