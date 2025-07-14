package com.example.proyectodemoviles.ui.viewModels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.proyectodemoviles.models.Appointments
import com.example.proyectodemoviles.models.ChatList
import com.example.proyectodemoviles.models.CreateChat
import com.example.proyectodemoviles.models.EnviarMensaje
import com.example.proyectodemoviles.repositories.ChatRepository
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar la lógica de negocio del chat en detalle.
 * Incluye funciones para cargar mensajes, enviar nuevos mensajes y crear nuevas conversaciones.
 */
class DetalleChatViewModel(private val application: Application) : AndroidViewModel(application) {
    // LiveData para los mensajes del chat
    private val _messages: MutableLiveData<ChatList> = MutableLiveData(arrayListOf())
    val messages: LiveData<ChatList> = _messages

    // LiveData para un nuevo mensaje enviado
    private val _newMessage: MutableLiveData<EnviarMensaje> = MutableLiveData()
    val newMessage: LiveData<EnviarMensaje> = _newMessage

    // LiveData para errores específicos de mensajes
    private val _mensajeError: MutableLiveData<String?> = MutableLiveData()
    val mensajeError: LiveData<String?> = _mensajeError

    // LiveData para errores generales
    private val _error: MutableLiveData<String?> = MutableLiveData()
    val error: LiveData<String?> = _error

    // LiveData para nueva conversación creada
    private val _newAppointment = MutableLiveData<Appointments?>()
    val newAppointment: LiveData<Appointments?> = _newAppointment

    // LiveData para estado de carga
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * Crea una nueva conversación (appointment) con un trabajador.
     * @param createChat Datos necesarios para crear la conversación
     */
    fun createNewAppointment(createChat: CreateChat) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val token = getAuthToken()

                if (token != null) {
                    val createdChat = ChatRepository.createChat(createChat, token)
                    _newAppointment.postValue(createdChat)
                } else {
                    handleAuthError()
                }
            } catch (e: Exception) {
                _error.postValue("Error al crear la conversación: ${e.message}")
                Log.e(TAG, "Error creando la conversación", e)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    /**
     * Carga los mensajes de un chat específico.
     * @param appointmentId ID de la cita/conversación
     */
    fun loadChatMessages(appointmentId: Int) {
        if (appointmentId <= 0) return

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val token = getAuthToken()

                if (token != null) {
                    val chatMessages = ChatRepository.getChatMessages(appointmentId, token)
                    _messages.postValue(chatMessages)
                } else {
                    handleAuthError()
                }
            } catch (e: Exception) {
                _error.postValue("Error al cargar los mensajes: ${e.message}")
                Log.e(TAG, "Error cargando mensajes del chat", e)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    /**
     * Valida un mensaje antes de enviarlo.
     * @param mensaje Texto del mensaje a validar
     * @return true si el mensaje es válido, false en caso contrario
     */
    fun validateInputMessage(mensaje: String): Boolean {
        _mensajeError.value = null

        if (mensaje.trim().isEmpty()) {
            _mensajeError.value = "No puede enviar un mensaje vacío"
            return false
        }

        return true
    }

    /**
     * Envía un mensaje en una conversación.
     * @param appointmentId ID de la cita/conversación
     * @param mensaje Contenido del mensaje a enviar
     */
    fun sendMessage(appointmentId: Int, mensaje: EnviarMensaje) {
        if (appointmentId <= 0) {
            _error.value = "ID de conversación inválido"
            return
        }

        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val token = getAuthToken()

                if (token != null) {
                    val enviarMensaje = ChatRepository.sendMessage(appointmentId, mensaje, token)
                    _newMessage.postValue(enviarMensaje)
                } else {
                    handleAuthError()
                }
            } catch (e: Exception) {
                _error.postValue("Error al enviar mensaje: ${e.message}")
                Log.e(TAG, "Error enviando mensaje", e)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    /**
     * Obtiene el token de autenticación de las preferencias compartidas.
     * @return Token de autenticación o null si no existe
     */
    private fun getAuthToken(): String? {
        val sharedPrefs = application.getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
        return sharedPrefs.getString("AUTH_TOKEN", null)
    }

    /**
     * Maneja un error de autenticación cuando no se encuentra el token.
     */
    private fun handleAuthError() {
        _error.postValue("No hay token de autenticación. Por favor, inicie sesión primero.")
        Log.e(TAG, "Token de autenticación no encontrado")
    }

    /**
     * Limpia los errores almacenados.
     */
    fun clearErrors() {
        _error.value = null
        _mensajeError.value = null
    }

    companion object {
        private const val TAG = "DetalleChatViewModel"
    }
}
