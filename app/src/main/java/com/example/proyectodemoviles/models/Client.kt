package com.example.proyectodemoviles.models

import com.google.gson.annotations.SerializedName

typealias ClientList = ArrayList<Client>
data class Client (
    @SerializedName("name")
    val name: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("profile")
    val profile: Profile? = null
)