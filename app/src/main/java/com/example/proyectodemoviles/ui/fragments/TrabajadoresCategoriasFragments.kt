package com.example.proyectodemoviles.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodemoviles.databinding.FragmentTrabajadoresCategoriasFragmentsBinding
import com.example.proyectodemoviles.models.Trabajadores
import com.example.proyectodemoviles.ui.adapters.TrabajadoresAdapter
import com.example.proyectodemoviles.ui.viewModels.TrabajadoresCategoriasFragmentsViewModel

class TrabajadoresCategoriasFragments : Fragment(), TrabajadoresAdapter.OnTrabajadorClick {
    private lateinit var binding: FragmentTrabajadoresCategoriasFragmentsBinding
    private val viewModel: TrabajadoresCategoriasFragmentsViewModel by viewModels()
    private val args: TrabajadoresCategoriasFragmentsArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrabajadoresCategoriasFragmentsBinding.inflate(inflater, container, false)
        val categoryId = args.categoryId
        val categoryName = args.categoryName
        setupRecyclerView()
        setupEventListeners()
        setupViewModelObservers(categoryName)
        viewModel.loadTrabajadoresByCategoria(categoryId)

        return binding.root
    }

    private fun setupEventListeners() {

    }

    private fun setupViewModelObservers(categoria: String) {
        viewModel.trabajadores.observe(viewLifecycleOwner) { trabajadores ->
            Log.d("TrabajadoresCategoriasFragments", "Trabajadores recibidos: ${trabajadores.size}")
            val adapter = binding.rvTrabajadores.adapter as TrabajadoresAdapter
            adapter.setData(trabajadores)

        }
        binding.titleTextView.text = "Trabajadores de : $categoria"

        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (errorMsg != null) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show()
            }
        }



    }

    private fun setupRecyclerView() {
        val adapter = TrabajadoresAdapter(arrayListOf())
        binding.rvTrabajadores.apply {
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
        adapter.setOnTrabajadorClickListener(this)
    }

    override fun onTrabajadorClick(trabajador: Trabajadores) {
        Toast.makeText(context, "Trabajador seleccionado: ${trabajador.user.name}", Toast.LENGTH_SHORT).show()
        val action = TrabajadoresCategoriasFragmentsDirections
            .actionTrabajadoresCategoriasFragmentsToDetalleTrabajador(trabajador.id)
        findNavController().navigate(action)

    }
}