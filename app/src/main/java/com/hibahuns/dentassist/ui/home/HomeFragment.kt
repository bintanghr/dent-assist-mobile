package com.hibahuns.dentassist.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
        homeViewModel.fetchClinics()
        homeViewModel.fetchProducts()

        return root
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    homeViewModel.clinics.collect { response ->
                        response?.data?.let { clinics ->
                            val rvData: MutableList<RvDataItem> = mutableListOf()

                            for (clinic in clinics) {
                                val title: String = clinic?.name ?: ""
                                val description: String = clinic?.address ?: ""
                                val imageUrl: String = clinic?.photo ?: ""
                                val data = RvDataItem(title, description, imageUrl)
                                rvData.add(data)
                            }

                            binding.recyclerView.apply {
                                layoutManager =
                                    LinearLayoutManager(
                                        context,
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                adapter = RvAdapter(rvData)
                            }
                        }
                    }
                }

                launch {
                    homeViewModel.products.collect { response ->
                        response?.data?.let { products ->
                            val rvData: MutableList<RvDataItem> = mutableListOf()

                            for (product in products) {
                                val title: String = product?.name ?: ""
                                val description: String = (product?.price.toString()) ?: ""
                                val imageUrl: String = product?.linkPhoto ?: ""
                                val data = RvDataItem(title, description, imageUrl)
                                rvData.add(data)
                            }

                            binding.productsRecyclerView.apply {
                                layoutManager =
                                    LinearLayoutManager(
                                        context,
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                adapter = RvAdapter(rvData)
                            }
                        }
                    }
                }
            }
        }
    }
//    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
//    override fun onBackPressed() {
//        val intent = Intent(this, MainActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//        startActivity(intent)
//        finish()
//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
//        super.onBackPressed()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}