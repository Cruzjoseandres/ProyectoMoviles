package com.example.proyectodemoviles.repositories

import com.example.proyectodemoviles.models.ChatList
import com.example.proyectodemoviles.models.EnviarMensaje

object ChatRepository {
    suspend fun getChatMessages(appointmentId: Int, token: String): ChatList {
        return RetrofitRepository
            .getJsonPlaceholderApi()
            .getChatByAppointmentId(appointmentId, "Bearer $token")
    }

    suspend fun sendMessage(id: Int, mensaje: EnviarMensaje, token: String): EnviarMensaje {
       return RetrofitRepository
            .getJsonPlaceholderApi()
            .sendMessage(id, mensaje, "Bearer $token")


    }

}
