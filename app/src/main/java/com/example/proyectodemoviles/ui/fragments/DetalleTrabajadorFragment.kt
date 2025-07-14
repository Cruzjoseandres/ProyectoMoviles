package com.example.proyectodemoviles.ui.fragments

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import com.example.proyectodemoviles.databinding.FragmentDetalleTrabajadorBinding
import com.example.proyectodemoviles.ui.adapters.ResenaAdapter
import com.example.proyectodemoviles.ui.viewModels.DetalleTrabajadorViewModel

class DetalleTrabajadorFragment : Fragment() {
    private lateinit var binding: FragmentDetalleTrabajadorBinding
    private val viewModel: DetalleTrabajadorViewModel by viewModels()
    private val args: DetalleTrabajadorFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetalleTrabajadorBinding.inflate(inflater, container, false)
        setupEventListeners()
        setupRecyclerView()
        setupObservers()
        val trabajadorId = args.id
        viewModel.loadTrabajador(trabajadorId)

        return binding.root
    }

    private fun setupEventListeners() {
        binding.btnContactar.setOnClickListener {
            val trabajador = viewModel.trabajador.value
            if (trabajador != null) {
                // Obtener el ID de categoría del trabajador (primera categoría si hay varias)
                val categoryId = if (trabajador.categories != null && trabajador.categories.isNotEmpty()) {
                    trabajador.categories[0].id
                } else {
                    1 // Valor por defecto
                }

                val action = DetalleTrabajadorFragmentDirections
                    .actionDetalleTrabajadorToDetalleChatFragment(
                        appointmentId = 0, // Es una conversación nueva
                        workId = 0, // No aplica para conversación nueva
                        workerId = trabajador.id,
                        categoryId = categoryId
                    )
                findNavController().navigate(action)
            } else {
                Toast.makeText(context, "Trabajador no encontrado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        val adapter = ResenaAdapter()
        binding.rvResenas.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupObservers() {
        viewModel.trabajador.observe(viewLifecycleOwner) { trabajador ->
            // Nombre del trabajador
            binding.tvNombre.text = "${trabajador.user.profile?.name ?: trabajador.user.name} ${trabajador.user.profile?.lastName ?: ""}"

            // Calificación y trabajos completados
            binding.tvCalificacion.text = " Calificación: ${trabajador.averageRating}★\n Trabajos Completados: ${trabajador.reviewsCount}"

            // Mostrar las categorías del trabajador si existen
            val categoriasTexto = if (trabajador.categories != null && trabajador.categories.isNotEmpty()) {
                // Unir los nombres de todas las categorías con comas
                trabajador.categories.joinToString(", ") { it.name }
            } else {
                "Sin categorías"
            }
            binding.tvCategorias.text = categoriasTexto

            // Cargar imagen del trabajador
            if (!trabajador.pictureUrl.isNullOrEmpty() && trabajador.pictureUrl != "null") {
                val requestOptions = RequestOptions()
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)

                // Obtener el token de autenticación
                val sharedPrefs = requireActivity().getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
                val token = sharedPrefs.getString("AUTH_TOKEN", "") ?: ""

                // Crear una URL con encabezado de autenticación para Glide
                val glideUrl = GlideUrl(
                    trabajador.pictureUrl,
                    LazyHeaders.Builder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                )

                // Cargar la imagen con el encabezado de autorización
                Glide.with(requireContext())
                    .load(glideUrl)
                    .apply(requestOptions)
                    .into(binding.imgPerfil)
            }

            // Actualizar lista de reseñas
            (binding.rvResenas.adapter as? ResenaAdapter)?.setData(trabajador.reviews)
        }

        // Observador de errores
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}
