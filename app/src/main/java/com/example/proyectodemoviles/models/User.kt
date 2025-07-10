package com.example.proyectodemoviles.models

import com.google.gson.annotations.SerializedName

typealias UserList = ArrayList<User>

data class User (
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