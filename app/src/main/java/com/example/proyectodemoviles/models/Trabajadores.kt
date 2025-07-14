package com.example.proyectodemoviles.models

import com.google.gson.annotations.SerializedName

typealias TrabajadoresList = ArrayList<Trabajadores>

data class Trabajadores(
    @SerializedName("id")
    val id: Int,

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("picture_url")
    val pictureUrl: String?,

    @SerializedName("average_rating")
    val averageRating: Float,

    @SerializedName("reviews_count")
    val reviewsCount: Int,

    @SerializedName("user")
    val user: User,

    @SerializedName("categories")
    val categories: ListCategory?,

    @SerializedName("reviews")
    val reviews: ReviewsList
)

