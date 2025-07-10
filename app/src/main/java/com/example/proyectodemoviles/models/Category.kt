package com.example.proyectodemoviles.models

import com.google.gson.annotations.SerializedName


typealias ListCategory = ArrayList<Category>

data class Category(
    @SerializedName("id")
    val id: Int,
    val name: String,
    @SerializedName("pivot")
    val pivot: Pivot? = null
)


