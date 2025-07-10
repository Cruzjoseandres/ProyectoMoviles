package com.example.proyectodemoviles.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodemoviles.databinding.ItemCategoryBinding
import com.example.proyectodemoviles.models.Category
import com.example.proyectodemoviles.models.ListCategory


class CategoryAdapter(
    items: ListCategory
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private var onCategoryClickListener: OnCategoryClick? = null
    private var filteredItems: ListCategory = ArrayList(items)
    private var allItems: ListCategory = ArrayList(items)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
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
        return filteredItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredItems[position]
        holder.bind(item, onCategoryClickListener)
    }

    fun setData(newData: ListCategory) {
        this.allItems = ArrayList(newData)
        this.filteredItems = ArrayList(newData)
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        filteredItems = if (query.isEmpty()) {
            ArrayList(allItems)
        } else {
            val filteredList = arrayListOf<Category>()
            for (item in allItems) {
                if (item.name.lowercase().contains(query.lowercase())) {
                    filteredList.add(item)
                }
            }
            filteredList
        }
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
