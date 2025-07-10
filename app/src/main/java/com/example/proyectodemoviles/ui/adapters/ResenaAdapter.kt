package com.example.proyectodemoviles.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodemoviles.databinding.ItemResenaBinding
import com.example.proyectodemoviles.models.Reviews
import com.example.proyectodemoviles.models.ReviewsList

class ResenaAdapter(
    private var items: ReviewsList = arrayListOf()
) : RecyclerView.Adapter<ResenaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemResenaBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setData(newData: ReviewsList) {
        items = newData
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemResenaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Reviews) {
            binding.tvNombreUsuario.text = "${review.user.name ?: ""} ${review.user?.lastName ?: ""}"
            binding.tvCalificacion.text = "${review.rating}★"
            binding.tvComentario.text = review.comment ?: "Sin comentario"
        }
    }
}