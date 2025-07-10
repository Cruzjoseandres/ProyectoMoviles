package com.example.proyectodemoviles.models

import com.google.gson.annotations.SerializedName


typealias ListAppointments = ArrayList<Appointments>
data class Appointments(
    val id: Int,
    val worker_id: Int,
    val user_id: Int,
    val appointment_date: String?,
    val appointment_time: String?,
    val category_selected_id: Int,
    val latitude: Double,
    val longitude: Double,
    val status: Int,
    @SerializedName("worker")
    val worker: Trabajadores? = null,
    @SerializedName("category")
    val category: Category? = null,
    @SerializedName("client")
    val client: Client? = null

)
