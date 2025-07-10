package com.example.proyectodemoviles.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
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
import com.example.proyectodemoviles.models.ListCategory

class SearchCategoriesFragment : Fragment(), CategoryAdapter.OnCategoryClick {
    private lateinit var binding: FragmentSearchCategoriesBinding
    private val viewModel: CategoryViewModel by viewModels()
    private lateinit var adapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializamos el adapter con una lista vacía pero válida
        adapter = CategoryAdapter(arrayListOf())

        // Configurar el RecyclerView primero
        setupRecyclerView()

        // Luego configuramos el resto de componentes
        setupSearchView()
        setupEventListeners()

        // Por último observamos los datos del ViewModel
        setupViewModelObservers()
        viewModel.loadCategories()
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (::adapter.isInitialized) {
                    adapter.filter(newText ?: "")
                }
                return true
            }
        })
    }

    private fun setupEventListeners() {
        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_searchCategoriesFragment_to_crearCategoriaFragment)
        }
    }

    private fun setupViewModelObservers() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            Log.d("SearchCategoriesFragment", "Categorías recibidas: ${categories.size}")
            if (::adapter.isInitialized) {
                adapter.setData(categories)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvCategorias.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.VERTICAL
            }
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )
            // Asignamos el adaptador al RecyclerView explícitamente
            adapter = this@SearchCategoriesFragment.adapter
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
