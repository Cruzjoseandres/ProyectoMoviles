package com.example.proyectodemoviles.models

import com.google.gson.annotations.SerializedName


typealias  ReviewsList = ArrayList<Reviews>
data class Reviews(
    val id: Int,
    val worker_id: Int,
    val user_id: Int,
    val appointment_id: Int,
    val rating: Int,
    val comment: String?,
    val is_done: Int,
    @SerializedName("user")
    val user: User
)
