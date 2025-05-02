package com.hibahuns.dentassist.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import androidx.core.content.ContextCompat
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
import com.hibahuns.dentassist.ui.login.LoginActivity
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val viewModel: HomeViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

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
        //apakah si user login apa enggak? jika enggak maka kembali ke LoginActivity
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

            binding.welcomeText.text = getGreetingMessage(user.username)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    homeViewModel.clinics.collect { response ->
                        response?.data?.let { clinics ->
//                            val rvData: MutableList<RvDataItem> = mutableListOf()

                            for (clinic in clinics) {
                                val id = clinic?.idClinic ?: ""
                                val title = clinic?.name ?: ""
                                val subTitle = clinic?.city ?: ""
                                val category = clinic?.noTelp ?: ""
                                val category2 = ""
                                val description = clinic?.address ?: ""
                                val subDescription = clinic?.rating.toString()
                                val subDescription2: List<String> = emptyList()
                                val keys: List<String> = emptyList()
                                val redirectUrl = clinic?.linkMaps ?: ""
                                val imageUrl = clinic?.photo ?: ""
                                val data = RvDataItem(id, title, subTitle, category, category2, description, subDescription, subDescription2, keys, redirectUrl, imageUrl, "clinic")
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
                                val id = product?.idProduct ?: ""
                                val title = product?.name ?: ""
                                val category = product?.ket ?: ""
                                val category2 = product?.shape ?: ""
                                val description = product?.description ?: ""
                                val subDescription = product?.dosis ?: ""
                                val subDescription2: List<String> = product?.notes ?: emptyList()
                                val keys: List<String> = product?.keys ?: emptyList()
                                val redirectUrl = ""
                                val imageUrl = product?.linkPhoto ?: ""

                                val localeID = Locale("in", "ID")
                                val numberFormat = NumberFormat.getNumberInstance(localeID)
                                val subTitle = "Rp ${numberFormat.format(product?.price)}"

                                val data = RvDataItem(id, title, subTitle, category, category2, description, subDescription, subDescription2, keys, redirectUrl, imageUrl, "product")

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
                                val id = article?.idArticle ?: ""
                                val title = article?.name ?: ""
                                val subTitle = article?.disease ?: ""
                                val category = article?.publicationDate ?: ""
                                val category2 = ""
                                val description = article?.contents ?: ""
                                val subDescription = ""
                                val subDescription2: List<String> = emptyList()
                                val keys: List<String> = article?.keys ?: emptyList()
                                val redirectUrl = article?.link ?: ""
                                val imageUrl: String = article?.imageUrl ?: "https://drive.google.com/uc?export=view&id=17s-a0tqsyQsDB5fmeR75ye2Z1vnDK1Wt"
//                                val imageUrl = "https://drive.google.com/uc?export=view&id=17s-a0tqsyQsDB5fmeR75ye2Z1vnDK1Wt"
                                val data = RvDataItem(id, title, subTitle, category, category2, description, subDescription, subDescription2, keys, redirectUrl, imageUrl, "article")
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

    private fun getColorFromAttr(context: Context, attr: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }

    private fun getGreetingMessage(username: String): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        return when (hour) {
            in 5..11 -> "Selamat Pagi, $username!"
            in 12..14 -> "Selamat Siang, $username!"
            in 15..17 -> "Selamat Sore, $username!"
            else -> "Selamat Malam, $username!"
        }
    }

    override fun onResume() {
        super.onResume()
        val activity = requireActivity() as AppCompatActivity

        val actionBarColor = getColorFromAttr(activity, R.attr.colorPrimaryTool)
        val titleColor = getColorFromAttr(activity, R.attr.colorAccent)

        activity.supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(actionBarColor))
            title = Html.fromHtml("<font color='${String.format("#%06X", 0xFFFFFF and titleColor)}'>DentAssist</font>", 1)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}