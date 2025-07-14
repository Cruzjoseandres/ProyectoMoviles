package com.example.proyectodemoviles.repositories

import com.example.proyectodemoviles.models.ListAppointments

object AppointmentsRepository {
    suspend fun getAppointments(token: String): ListAppointments {
        return RetrofitRepository
            .getJsonPlaceholderApi()
            .getAppointments("Bearer $token")
    }


}