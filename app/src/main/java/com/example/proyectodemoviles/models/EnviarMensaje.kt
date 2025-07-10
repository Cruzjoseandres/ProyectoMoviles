package com.example.proyectodemoviles.models

import com.google.gson.annotations.SerializedName


data class EnviarMensaje(
    @SerializedName("message")
    val message : String,
    @SerializedName("receiver_id")
    val receiver_id: Int
)
