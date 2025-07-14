package com.example.proyectodemoviles.ui.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodemoviles.databinding.FragmentChatBinding
import com.example.proyectodemoviles.models.Appointments
import com.example.proyectodemoviles.ui.adapters.ChatsListAdapter
import com.example.proyectodemoviles.ui.viewModels.ChatViewModel

class ChatFragment : Fragment(), ChatsListAdapter.OnChatClick {
    private lateinit var binding: FragmentChatBinding
    private val viewModel: ChatViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        viewModel.loadChatsUser()
        setupEventListeners()
        setupObservers()
        setupRecyclerView()
        return  binding.root
    }

    private fun setupRecyclerView() {
        val adapter = ChatsListAdapter(arrayListOf())
        binding.rvChats.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.VERTICAL
            }
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL
                )
            )
        }
        adapter.setOnChatClickListener(this)

    }

    private fun setupObservers() {
        viewModel.chats.observe(viewLifecycleOwner) { chats ->
            val adapter = binding.rvChats.adapter as ChatsListAdapter
            adapter.setData(chats)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (errorMsg != null) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun setupEventListeners() {

    }

    override fun onchatClick(citas: Appointments) {
        Toast.makeText(context, "Chat seleccionado: ${citas.worker?.user?.name}", Toast.LENGTH_SHORT).show()
        val action = ChatFragmentDirections
            .actionChatFragmentToDetalleChatFragment(
                appointmentId = citas.id,
                workId = citas.worker_id,
                workerId = 0,  // Estos valores no se usarán en chats existentes
                categoryId = 0  // Estos valores no se usarán en chats existentes
            )
        findNavController().navigate(action)
    }
}