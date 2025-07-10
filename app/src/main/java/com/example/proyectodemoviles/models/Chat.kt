package com.example.proyectodemoviles.models

import com.google.gson.annotations.SerializedName

typealias ChatList = ArrayList<Chat>

data class Chat(
    @SerializedName("id")
    val id: Int,

    @SerializedName("appointment_id")
    val appointment_id: Int,

    @SerializedName("sender_id")
    val sender_id: Int,

    @SerializedName("receiver_id")
    val receiver_id: Int,

    @SerializedName("date_sent")
    val date_sent: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("appointment")
    val appointment: Appointments? = null,

    @SerializedName("sender")
    val sender: Sender? = null,

    @SerializedName("receiver")
    val receiver: Receiver? = null
)


