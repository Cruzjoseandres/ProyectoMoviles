package com.example.proyectodemoviles.models

import com.google.gson.annotations.SerializedName

typealias ListSender = ArrayList<Sender>
data class Sender(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String
)
