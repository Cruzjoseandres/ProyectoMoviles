package com.example.proyectodemoviles.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectodemoviles.databinding.ChatListItemBinding
import com.example.proyectodemoviles.models.ListAppointments
import com.example.proyectodemoviles.models.Appointments


class ChatsListAdapter(
    var items: ListAppointments
) : RecyclerView.Adapter<ChatsListAdapter.ViewHolder>() {
    private var onChatClickListener: OnChatClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =LayoutInflater.from(parent.context)
        val binding = ChatListItemBinding.inflate(
            inflater,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    fun setOnChatClickListener(listener: OnChatClick) {
        this.onChatClickListener = listener
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onChatClickListener)
    }

    fun setData(newData: ListAppointments) {
        this.items = newData
        notifyDataSetChanged()
    }
    class ViewHolder(private val binding: ChatListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Appointments, listener: OnChatClick?) {
            binding.nombreTrabajador.text = "${item.worker?.user?.name} ${item.worker?.user?.profile?.lastName}"

            if (item.worker?.pictureUrl != null && item.worker?.pictureUrl != "null") {
                Glide.with(binding.root.context)
                    .load(item.worker?.pictureUrl)
                    .into(binding.imagenTrabajador)
            }
            binding.ratingTrabajador.text = "Calificación: ${item.worker?.averageRating}"
            binding.reviewsCount.text = "Reseñas: ${item.worker?.reviewsCount}"

            binding.root.setOnClickListener {
                listener?.onchatClick(item)
            }
        }
    }

    interface OnChatClick {
        fun onchatClick(citas: Appointments)

    }
}
