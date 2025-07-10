package com.example.proyectodemoviles.ui.fragments

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.proyectodemoviles.databinding.FragmentDetalleTrabajadorBinding
import com.example.proyectodemoviles.ui.adapters.ResenaAdapter
import com.example.proyectodemoviles.ui.viewModels.DetalleTrabajadorViewModel
import kotlin.apply
import kotlin.getValue

class DetalleTrabajadorFragment : Fragment() {
    private lateinit var binding: FragmentDetalleTrabajadorBinding
    private val viewModel: DetalleTrabajadorViewModel by viewModels()
    private val args: DetalleTrabajadorFragmentArgs by navArgs()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetalleTrabajadorBinding.inflate(inflater, container, false)
        setupRecyclerView()
        setupObservers()
        val  trabajadorId = args.id
        viewModel.loadTrabajador(trabajadorId)


        return binding.root
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

            binding.tvNombre.text = "${trabajador.user.profile?.name ?: trabajador.user.name} ${trabajador.user.profile?.lastName ?: ""}"

            binding.tvCalificacion.text =
                " Calificación: ${trabajador.averageRating}★\n Trabajos Completados: ${trabajador.reviewsCount}"

            val categorias = trabajador.categories.joinToString(", ") { it.name }
            binding.tvCategorias.text = categorias

            if (trabajador.pictureUrl != null && trabajador.pictureUrl != "null") {
                val sharedPrefs = requireActivity().getSharedPreferences("AUTH_PREFS", Context.MODE_PRIVATE)
                val token = sharedPrefs.getString("AUTH_TOKEN", null)

                val requestOptions = RequestOptions()
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)

                Glide.with(requireContext())
                    .load(trabajador.pictureUrl)
                    .apply(requestOptions)
                    .into(binding.imgPerfil)
            }

            val adapter = binding.rvResenas.adapter as? ResenaAdapter
            adapter?.setData(trabajador.reviews)
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            }
        }
    }
}
