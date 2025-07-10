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

        setupRecyclerView(userId)
        setupEventListeners(appointmentId, workId)
        setupObservers()

        // Cargar mensajes iniciales
        viewModel.loadChatMessages(appointmentId)

        // Configurar actualización periódica
        setupPeriodicRefresh()

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
        // Iniciar la actualización periódica
        handler.postDelayed(chatRefreshRunnable, 5000)
    }

    override fun onPause() {
        super.onPause()
        // Detener la actualización periódica cuando el fragmento no es visible
        handler.removeCallbacks(chatRefreshRunnable)
    }

    private fun setupEventListeners(appointmentId: Int, receiverId: Int) {
        binding.btnSendMessage.setOnClickListener {
            if (validateInputMessage()) {
                sendMessage(appointmentId, receiverId)
            }
        }
    }

    private fun setupObservers() {
        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            chatAdapter.setData(messages)
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
    }

    fun validateInputMessage(): Boolean {
        val mensaje = binding.textInputLayout.editText?.text.toString()
        return viewModel.validateInputMessage(mensaje)
    }

    fun sendMessage(appointmentId: Int, receiverId: Int) {
        val enviarMensaje = EnviarMensaje(
            message = binding.textInputLayout.editText?.text.toString(),
            receiver_id = receiverId
        )
        viewModel.sendMenssage(appointmentId, enviarMensaje)
    }
}