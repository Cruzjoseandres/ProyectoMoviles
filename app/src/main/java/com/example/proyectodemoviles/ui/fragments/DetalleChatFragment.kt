package com.example.proyectodemoviles.ui.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodemoviles.databinding.FragmentDetalleChatBinding
import com.example.proyectodemoviles.models.CreateChat
import com.example.proyectodemoviles.models.EnviarMensaje
import com.example.proyectodemoviles.ui.adapters.ChatMessagesAdapter
import com.example.proyectodemoviles.ui.viewModels.DetalleChatViewModel

class DetalleChatFragment : Fragment() {
    private lateinit var binding: FragmentDetalleChatBinding
    private val viewModel: DetalleChatViewModel by viewModels()
    private val args by navArgs<DetalleChatFragmentArgs>()
    private lateinit var chatAdapter: ChatMessagesAdapter
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var chatRefreshRunnable: Runnable
    private var appointmentId = 0
    private var receiverUserId = 0  // Cambiado de receiverId a receiverUserId para mayor claridad
    private var isNewConversation = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetalleChatBinding.inflate(inflater, container, false)
        appointmentId = args.appointmentId
        val workId = args.workId

        // Obtener ID del usuario actual
        val sharedPrefs = requireActivity().getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
        val userId = sharedPrefs.getInt("USER_ID", -1)

        if (appointmentId > 0) {
            // Conversación existente - necesitamos obtener el user_id del trabajador
            isNewConversation = false
            // El receiverUserId se inicializará cuando observemos los mensajes del chat
            viewModel.loadChatMessages(appointmentId)
        } else {
            // Nueva conversación desde perfil de trabajador
            isNewConversation = true

            // Si es una conversación nueva, creamos un appointment
            val creaChat = CreateChat(
                args.workerId,
                args.categoryId
            )
            viewModel.createNewAppointment(creaChat)
        }

        setupRecyclerView(userId)
        setupEventListeners()
        setupObservers()

        if (!isNewConversation) {
            viewModel.loadChatMessages(appointmentId)
            setupPeriodicRefresh()
        }

        return binding.root
    }

    private fun setupRecyclerView(currentUserId: Int) {
        chatAdapter = ChatMessagesAdapter(arrayListOf(), currentUserId)
        binding.rvChatMessages.apply {
            this.adapter = chatAdapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.VERTICAL
                stackFromEnd = true // Para que se vean los mensajes más recientes
            }
        }
    }

    private fun setupPeriodicRefresh() {
        chatRefreshRunnable = Runnable {
            viewModel.loadChatMessages(appointmentId)
            handler.postDelayed(chatRefreshRunnable, 5000) // 5 segundos
        }
    }

    override fun onResume() {
        super.onResume()
        // Iniciar la actualización periódica solo si tenemos un appointmentId válido
        if (appointmentId > 0) {
            handler.postDelayed(chatRefreshRunnable, 5000)
        }
    }

    override fun onPause() {
        super.onPause()
        // Detener la actualización periódica cuando el fragmento no es visible
        handler.removeCallbacks(chatRefreshRunnable)
    }

    private fun setupEventListeners() {
        binding.btnSendMessage.setOnClickListener {
            if (validateInputMessage()) {
                if (isNewConversation && appointmentId <= 0) {
                    // Si es una conversación nueva y aún no tenemos appointmentId
                    val appointment = viewModel.newAppointment.value
                    if (appointment != null && appointment.id > 0) {
                        appointmentId = appointment.id
                        // Obtenemos el user_id del trabajador desde el objeto appointment
                        if (appointment.worker?.userId != null) {
                            receiverUserId = appointment.worker.userId
                            sendMessage(appointmentId, receiverUserId)
                        } else {
                            Toast.makeText(context, "Error: No se pudo identificar al destinatario", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Error: No se pudo crear la conversación", Toast.LENGTH_SHORT).show()
                    }
                } else if (receiverUserId > 0) {
                    // Si ya tenemos el userId del receptor
                    sendMessage(appointmentId, receiverUserId)
                } else {
                    Toast.makeText(context, "Error: No se ha identificado al destinatario", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            chatAdapter.setData(messages)

            // Si tenemos mensajes y es una conversación existente, intentamos obtener el user_id del trabajador
            if (messages.isNotEmpty() && !isNewConversation && receiverUserId == 0) {
                // Buscamos un mensaje que no sea del usuario actual para identificar al otro participante
                val sharedPrefs = requireActivity().getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
                val currentUserId = sharedPrefs.getInt("USER_ID", -1)

                for (message in messages) {
                    if (message.sender_id != currentUserId) {
                        receiverUserId = message.sender_id
                        break
                    } else if (message.receiver_id != currentUserId) {
                        receiverUserId = message.receiver_id
                        break
                    }
                }
            }

            // Desplazar automáticamente al último mensaje
            if (messages.isNotEmpty()) {
                binding.rvChatMessages.scrollToPosition(messages.size - 1)
            }
        }

        viewModel.newMessage.observe(viewLifecycleOwner) { mensaje ->
            // Limpiar el campo de texto después de enviar el mensaje
            binding.textInputLayout.editText?.text?.clear()

            // Recargar mensajes para ver el nuevo mensaje
            viewModel.loadChatMessages(appointmentId)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.mensajeError.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.newAppointment.observe(viewLifecycleOwner) { appointment ->
            appointment?.let {
                // Guardar el nuevo appointmentId
                appointmentId = appointment.id
                isNewConversation = false

                // Obtener el user_id del trabajador desde el objeto appointment
                if (appointment.worker?.userId != null) {
                    receiverUserId = appointment.worker.userId
                }

                // Ahora podemos cargar o iniciar el chat normalmente
                setupPeriodicRefresh()
                viewModel.loadChatMessages(appointmentId)
            }
        }
    }

    fun validateInputMessage(): Boolean {
        val mensaje = binding.textInputLayout.editText?.text.toString()
        return viewModel.validateInputMessage(mensaje)
    }

    fun sendMessage(appointmentId: Int, receiverUserId: Int) {
        // Depuración para verificar los IDs
        val enviarMensaje = EnviarMensaje(
            message = binding.textInputLayout.editText?.text.toString(),
            receiver_id = receiverUserId
        )

        viewModel.sendMessage(appointmentId, enviarMensaje)
    }
}