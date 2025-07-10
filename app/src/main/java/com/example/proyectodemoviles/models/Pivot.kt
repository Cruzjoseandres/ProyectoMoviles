package com.example.proyectodemoviles.models

import com.google.gson.annotations.SerializedName


typealias PivotList = ArrayList<Pivot>
data class Pivot(
    @SerializedName("work_id")
    val worker_id: Int,

    @SerializedName("category_id")
    val category_id: Int,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String
)