package com.example.proyectodemoviles.models

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String,
    @SerializedName("profile")
    val profile: Profile
)




