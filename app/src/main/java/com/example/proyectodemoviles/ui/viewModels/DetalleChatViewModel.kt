package com.example.proyectodemoviles.ui.viewModels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.proyectodemoviles.models.Appointments
import com.example.proyectodemoviles.models.Chat
import com.example.proyectodemoviles.models.ChatList
import com.example.proyectodemoviles.models.EnviarMensaje
import com.example.proyectodemoviles.repositories.ChatRepository
import kotlinx.coroutines.launch

class DetalleChatViewModel(private val application: Application) : AndroidViewModel(application) {
    private val _messages: MutableLiveData<ChatList> = MutableLiveData(arrayListOf())
    val messages: LiveData<ChatList> = _messages

    private val _newMessage: MutableLiveData<EnviarMensaje> = MutableLiveData()
    val newMessage: LiveData<EnviarMensaje> = _newMessage

    private val _mensajeError : MutableLiveData<String?> = MutableLiveData()
    val  mensajeError: LiveData<String?> =_mensajeError

    private val _error: MutableLiveData<String?> = MutableLiveData()
    val error: LiveData<String?> = _error


    fun loadChatMessages(appointmentId: Int) {
        viewModelScope.launch {
            try {
                val sharedPrefs = application.getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
                val token = sharedPrefs.getString("AUTH_TOKEN", null)

                if (token != null) {
                    val chatMessages = ChatRepository.getChatMessages(appointmentId, token)
                    _messages.postValue(chatMessages)
                    _error.postValue(null)
                } else {
                    _error.postValue("No hay token de autenticación. Por favor, inicie sesión primero.")
                    Log.e("DetalleChatViewModel", "Token de autenticación no encontrado")
                }
            } catch (e: Exception) {
                _error.postValue("Error al cargar los mensajes: ${e.message}")
                Log.e("DetalleChatViewModel", "Error loading chat messages", e)
            }
        }
    }

    fun validateInputMessage(mensaje: String): Boolean {
        var isValid = true
        if (mensaje.isEmpty()) {
            _mensajeError.value = "No puede enviar un mensaje vacío"
            isValid = false
        } else {
            _mensajeError.value = null
        }
        return isValid
    }

    fun sendMenssage(appointmentId: Int, mensaje: EnviarMensaje){
        viewModelScope.launch {
            try {
                val sharedPrefs = application.getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
                val token = sharedPrefs.getString("AUTH_TOKEN", null)

                if (token != null) {
                    val enviarMensaje = ChatRepository.sendMessage(appointmentId,mensaje, token)
                    _newMessage.postValue(enviarMensaje)
                    _error.postValue(null)
                } else {
                    _error.postValue("No hay token de autenticación. Por favor, inicie sesión primero.")
                    Log.e("DetalleChatViewModel", "Token de autenticación no encontrado")
                }
            } catch (e: Exception) {
                _error.postValue("Error al enviar mensaje: ${e.message}")
                Log.e("DetalleChatViewModel", "Error loading messages", e)
            }
        }

    }

    /*fun sendMessage(appointmentId: Int, message: Chat) {
        viewModelScope.launch {
            _loading.postValue(true)
            try {
                val sharedPrefs = application.getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
                val token = sharedPrefs.getString("AUTH_TOKEN", null)

                if (token != null) {
                    val response = ChatRepository.sendMessage(appointmentId, message, token)
                    _newMessage.postValue(response)

                    // Añadir el mensaje nuevo a la lista de mensajes
                    val currentMessages = _messages.value ?: arrayListOf()
                    currentMessages.add(Chat(
                        id = response.id,
                        appointment_id = response.appointment_id,
                        sender_id = response.sender_id,
                        receiver_id = response.receiver_id,
                        date_sent = response.date_sent,
                        message = response.message,
                        sender = response.sender,
                        receiver = response.receiver,
                        appointment = response.appointment
                    ))
                    _messages.postValue(currentMessages)

                    _error.postValue(null)
                } else {
                    _error.postValue("No hay token de autenticación. Por favor, inicie sesión primero.")
                    Log.e("DetalleChatViewModel", "Token de autenticación no encontrado")
                }
            } catch (e: Exception) {
                _error.postValue("Error al enviar el mensaje: ${e.message}")
                Log.e("DetalleChatViewModel", "Error sending message", e)
            } finally {
                _loading.postValue(false)
            }
        }
    }*/
}

