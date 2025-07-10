package com.example.proyectodemoviles.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectodemoviles.models.Category
import com.example.proyectodemoviles.ui.adapters.CategoryAdapter
import com.example.proyectodemoviles.ui.viewModels.CategoryViewModel
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodemoviles.R
import com.example.proyectodemoviles.databinding.FragmentSearchCategoriesBinding

class SearchCategoriesFragment : Fragment(), CategoryAdapter.OnCategoryClick {
    private lateinit var binding: FragmentSearchCategoriesBinding
    private val viewModel: CategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchCategoriesBinding.inflate(inflater, container, false)
        setupRecyclerView()
        setupEventListeners()
        setupViewModelObservers()
        viewModel.loadCategories()
        return binding.root
    }


    private fun setupEventListeners() {
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_searchCategoriesFragment_to_crearCategoriaFragment)
        }
    }

    private fun setupViewModelObservers() {
        viewModel.categories.observe(viewLifecycleOwner) {
            Log.d("SearchCategoriesFragment", "Categorías recibidas: ${it.size}")
            val adapter = binding.rvCategorias.adapter as CategoryAdapter
            adapter.setData(it)
        }

    }

    private fun setupRecyclerView() {
        val adapter = CategoryAdapter(arrayListOf())
        binding.rvCategorias.apply {
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
        adapter.setOnCategoryClickListener(this)
    }

    override fun onCategoryClick(categoria: Category) {
        Toast.makeText(context, "Categoría seleccionada: ${categoria.name}", Toast.LENGTH_SHORT).show()
        val action = SearchCategoriesFragmentDirections
                .actionSearchCategoriesFragmentToTrabajadoresCategoriasFragments(categoria.id, categoria.name)
        findNavController().navigate(action)

    }
}
