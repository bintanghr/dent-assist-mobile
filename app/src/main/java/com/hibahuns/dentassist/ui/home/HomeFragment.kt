package com.hibahuns.dentassist.ui.home

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hibahuns.dentassist.R
import com.hibahuns.dentassist.databinding.FragmentHomeBinding
import com.hibahuns.dentassist.ui.ViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private var clinicsRvData: MutableList<RvDataItem> = mutableListOf()
    private var productsRvData: MutableList<RvDataItem> = mutableListOf()
    private var articlesRvData: MutableList<RvDataItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        homeViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val welcomeText: TextView = binding.welcomeText
        homeViewModel.welcomeText.observe(viewLifecycleOwner) {
            welcomeText.text = it
        }

        setupObserver()
        homeViewModel.fetchArticles()
        homeViewModel.fetchClinics()
        homeViewModel.fetchProducts()

        binding.btnShowAllClinics.setOnClickListener {
            navigateToClinics(clinicsRvData)
        }
        binding.btnShowAllProducts.setOnClickListener {
            navigateToClinics(productsRvData)
        }
        binding.btnShowAllArticles.setOnClickListener {
            navigateToClinics(articlesRvData)
        }

        return root
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    homeViewModel.clinics.collect { response ->
                        response?.data?.let { clinics ->
//                            val rvData: MutableList<RvDataItem> = mutableListOf()

                            for (clinic in clinics) {
                                val id: String = clinic?.idClinic ?: ""
                                val title: String = clinic?.name ?: ""
                                val description: String = clinic?.address ?: ""
                                val imageUrl: String = clinic?.photo ?: ""
                                val data = RvDataItem(id, title, description, imageUrl)
                                clinicsRvData.add(data)
                            }

                            binding.recyclerView.apply {
                                layoutManager =
                                    LinearLayoutManager(
                                        context,
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                adapter = RvAdapter(clinicsRvData)
                            }
                        }
                    }
                }

                launch {
                    homeViewModel.products.collect { response ->
                        response?.data?.let { products ->
//                            val rvData: MutableList<RvDataItem> = mutableListOf()

                            for (product in products) {
                                val id: String = product?.idProduct ?: ""
                                val title: String = product?.name ?: ""
                                val description: String = (product?.price.toString())
                                val imageUrl: String = product?.linkPhoto ?: ""
                                val data = RvDataItem(id, title, description, imageUrl)
                                productsRvData.add(data)
                            }

                            binding.productsRecyclerView.apply {
                                layoutManager =
                                    LinearLayoutManager(
                                        context,
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                adapter = RvAdapter(productsRvData)
                            }
                        }
                    }
                }

                launch {
                    homeViewModel.articles.collect { response ->
                        response?.data?.let { articles ->
//                            val rvData: MutableList<RvDataItem> = mutableListOf()

                            for (article in articles) {
                                val id: String = article?.idProduct ?: ""
                                val title: String = article?.disease ?: ""
                                val description: String = article?.name ?: ""
//                                val imageUrl: String = article?.linkPhoto ?: "https://drive.google.com/uc?export=view&id=17s-a0tqsyQsDB5fmeR75ye2Z1vnDK1Wt"
                                val imageUrl = "https://drive.google.com/uc?export=view&id=17s-a0tqsyQsDB5fmeR75ye2Z1vnDK1Wt"
                                val data = RvDataItem(id, title, description, imageUrl)
                                articlesRvData.add(data)
                            }

                            binding.articlesRecyclerView.apply {
                                layoutManager =
                                    LinearLayoutManager(
                                        context,
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                adapter = RvAdapter(articlesRvData)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun navigateToClinics(data: MutableList<RvDataItem>) {
        val bundle = Bundle().apply {
            putParcelableArrayList("datas", ArrayList(data))
        }

        findNavController().navigate(
            R.id.navigation_clinics,
            bundle,
            NavOptions.Builder()
                .setPopUpTo(R.id.navigation_home, false)
                .build()
        )
    }


    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
            title = Html.fromHtml("<font color='#EA7676'>DentAssist</font>", 1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}