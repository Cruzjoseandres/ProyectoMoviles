package com.example.proyectodemoviles.models

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("type")
    val type: Int
)