package com.example.proyectodemoviles.models

import com.google.gson.annotations.SerializedName

data class CreateChat(
    @SerializedName("worker_id")
    val worker_id: Int,

    @SerializedName("category_selected_id")
    val category_id: Int
)