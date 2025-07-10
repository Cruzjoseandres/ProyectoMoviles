package com.example.proyectodemoviles.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectodemoviles.databinding.ItemTrabajadorBinding
import com.example.proyectodemoviles.models.Trabajadores
import com.example.proyectodemoviles.models.TrabajadoresList


class TrabajadoresAdapter(
    var items: TrabajadoresList
) : RecyclerView.Adapter<TrabajadoresAdapter.ViewHolder>() {
    private var onTrabajadorClickListener: OnTrabajadorClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTrabajadorBinding.inflate(
            inflater,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    fun setOnTrabajadorClickListener(listener: OnTrabajadorClick) {
        this.onTrabajadorClickListener = listener
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onTrabajadorClickListener)
    }

    fun setData(newData: TrabajadoresList) {
        this.items = newData
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemTrabajadorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Trabajadores, listener: OnTrabajadorClick?) {
            binding.nombreTrabajador.text = "${item.user.name} ${item.user.profile?.lastName}"

            if (item.pictureUrl != null && item.pictureUrl != "null") {
                Glide.with(binding.root.context)
                    .load(item.pictureUrl)
                    .into(binding.imagenTrabajador)
            }
            binding.ratingTrabajador.text = "Calificación: ${item.averageRating}"
            binding.reviewsCount.text = "Reseñas: ${item.reviewsCount}"

            binding.root.setOnClickListener {
                listener?.onTrabajadorClick(item)
            }
        }
    }

    interface OnTrabajadorClick {
        fun onTrabajadorClick(trabajador: Trabajadores)
    }
}
