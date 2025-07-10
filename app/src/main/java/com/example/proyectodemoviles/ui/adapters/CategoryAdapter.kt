package com.example.proyectodemoviles.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodemoviles.databinding.ItemCategoryBinding
import com.example.proyectodemoviles.models.Category
import com.example.proyectodemoviles.models.ListCategory


class CategoryAdapter(
    var items: ListCategory
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private var onCategoryClickListener: OnCategoryClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(
            inflater,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    fun setOnCategoryClickListener(listener: OnCategoryClick) {
        this.onCategoryClickListener = listener
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onCategoryClickListener)
    }

    fun setData(newData: ListCategory) {
        this.items = newData
        notifyDataSetChanged()
    }
    class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Category, listener: OnCategoryClick?) {
            binding.categoriasNombre.text = item.name
            binding.root.setOnClickListener {
                listener?.onCategoryClick(item)
            }
        }
    }

    interface OnCategoryClick {
        fun onCategoryClick(categoria: Category)

    }
}
