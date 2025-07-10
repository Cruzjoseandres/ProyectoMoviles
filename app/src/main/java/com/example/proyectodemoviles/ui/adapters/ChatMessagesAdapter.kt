package com.example.proyectodemoviles.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodemoviles.R
import com.example.proyectodemoviles.databinding.ItemChatMessageBinding
import com.example.proyectodemoviles.models.Chat
import com.example.proyectodemoviles.models.ChatList
import java.text.SimpleDateFormat
import java.util.Locale

class ChatMessagesAdapter(
    var items: ChatList,
    private val currentUserId: Int
) : RecyclerView.Adapter<ChatMessagesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChatMessageBinding.inflate(
            inflater,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, currentUserId)
    }

    fun setData(newData: ChatList) {
        this.items = newData
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemChatMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Chat, currentUserId: Int) {
            // Establecer el texto del mensaje
            binding.tvMessageText.text = message.message

            // Formatear la fecha
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            try {
                val date = inputFormat.parse(message.date_sent)
                binding.tvMessageTime.text = outputFormat.format(date ?: System.currentTimeMillis())
            } catch (e: Exception) {
                binding.tvMessageTime.text = message.date_sent
            }

            // Nombre del remitente
            val senderName = message.sender?.name ?: "Usuario"
            binding.tvMessageSender.text = senderName

            // Ajustar la apariencia según si el mensaje es del usuario actual o no
            val isCurrentUser = message.sender_id == currentUserId
            val context = itemView.context

            if (isCurrentUser) {
                binding.cardMessage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.sent_message_bg))
                binding.tvMessageSender.visibility = View.GONE
                val layoutParams = binding.cardMessage.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.setMargins(80, layoutParams.topMargin, 10, layoutParams.bottomMargin)
                binding.cardMessage.layoutParams = layoutParams
            } else {
                binding.cardMessage.setCardBackgroundColor(ContextCompat.getColor(context, R.color.received_message_bg))
                binding.tvMessageSender.visibility = View.VISIBLE
                val layoutParams = binding.cardMessage.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.setMargins(10, layoutParams.topMargin, 80, layoutParams.bottomMargin)
                binding.cardMessage.layoutParams = layoutParams
            }
        }
    }
}