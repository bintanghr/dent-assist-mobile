package com.hibahuns.dentassist.ui.prediction

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.hibahuns.dentassist.R
import com.hibahuns.dentassist.data.api.response.PredictionData
import com.hibahuns.dentassist.databinding.FragmentPredictionBinding
import com.hibahuns.dentassist.ui.ViewModelFactory
import kotlin.math.round

@Suppress("DEPRECATION")
class PredictionFragment : Fragment() {
    private lateinit var predictionViewModel: PredictionViewModel
    private var _binding: FragmentPredictionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPredictionBinding.inflate(inflater, container, false)
        val root: View = binding.root
        predictionViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))[PredictionViewModel::class.java]

        val predictionData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("predictionData", PredictionData::class.java)
        } else {
            arguments?.getParcelable("predictionData")
        }

        predictionData?.let { data ->
            with(binding) {
                diseaseName.text = data.label
                description.text = data.explanation
                val accuracy = "${data.confidenceScore?.let { round(it).toInt().toString() }}%"
                accuracyPercentage.text = accuracy

                Glide.with(requireContext())
                    .load(data.signedUrl)
                    .placeholder(R.drawable.image_preview)
                    .error(R.drawable.image_preview)
                    .into(diseaseImage)

                data.clinic?.let { clinicData ->
                    val recommendationItem = layoutInflater.inflate(R.layout.recomendation_item, clinicDiv, false)
                    val title = recommendationItem.findViewById<TextView>(R.id.item_title)
                    val description = recommendationItem.findViewById<TextView>(R.id.item_description)
                    val image = recommendationItem.findViewById<ShapeableImageView>(R.id.item_img)
                    val tag1 = recommendationItem.findViewById<TextView>(R.id.tag_1)
                    val tag2 = recommendationItem.findViewById<TextView>(R.id.tag_2)

                    if (clinicData.name != null) {
                        title.text = clinicData.name
                        description.text = clinicData.address
                        tag1.text = getString(R.string.lorem_ipsum)
                        tag2.text = getString(R.string.lorem_ipsum)
                        tag1.visibility = View.GONE
                        tag2.visibility = View.GONE
                        Glide.with(requireContext())
                            .load(clinicData.photo)
                            .placeholder(R.drawable.image_preview)
                            .error(R.drawable.image_preview)
                            .into(image)
                        clinicDiv.addView(recommendationItem)
                    } else {
                        val failedGetClinic = TextView(context).apply {
                            text = getString(R.string.gagal_mendapatkan_data_klinik)
                            textSize = 16f
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                        }

                        clinicDiv.addView(failedGetClinic)
                    }
                }

                if (data.products?.get(0)?.disease != null) {
                    for (productData in data.products) {
                        val recommendationItem = layoutInflater.inflate(R.layout.recomendation_item, productDiv, false)
                        val title = recommendationItem.findViewById<TextView>(R.id.item_title)
                        val description = recommendationItem.findViewById<TextView>(R.id.item_description)
                        val image = recommendationItem.findViewById<ShapeableImageView>(R.id.item_img)
                        val tag1 = recommendationItem.findViewById<TextView>(R.id.tag_1)
                        val tag2 = recommendationItem.findViewById<TextView>(R.id.tag_2)

                        title.text = productData?.name
                        description.text = productData?.ket
                        tag1.text = getString(R.string.lorem_ipsum)
                        tag2.text = getString(R.string.lorem_ipsum)
                        tag1.visibility = View.GONE
                        tag2.visibility = View.GONE
                        Glide.with(requireContext())
                            .load(productData?.linkPhoto)
                            .placeholder(R.drawable.image_preview)
                            .error(R.drawable.image_preview)
                            .into(image)

                        productDiv.addView(recommendationItem)
                    }
                } else {
                    val productNotFound = TextView(context).apply {
                        text = context.getString(R.string.product_not_found)
                        textSize = 16f
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    }
                    productDiv.addView(productNotFound)
                }

                if (data.articles?.get(0)?.title != null) {
                    for (articleData in data.articles) {
                        val recommendationItem = layoutInflater.inflate(R.layout.recomendation_item, articleDiv, false)
                        val title = recommendationItem.findViewById<TextView>(R.id.item_title)
                        val description = recommendationItem.findViewById<TextView>(R.id.item_description)
                        val image = recommendationItem.findViewById<ShapeableImageView>(R.id.item_img)
                        val tag1 = recommendationItem.findViewById<TextView>(R.id.tag_1)
                        val tag2 = recommendationItem.findViewById<TextView>(R.id.tag_2)

                        title.text = articleData?.name
                        description.visibility = View.GONE
                        tag1.text = getString(R.string.lorem_ipsum)
                        tag2.text = getString(R.string.lorem_ipsum)
                        tag1.visibility = View.GONE
                        tag2.visibility = View.GONE
                        Glide.with(requireContext())
                            .load(articleData?.imageUrl)
                            .placeholder(R.drawable.image_preview)
                            .error(R.drawable.image_preview)
                            .into(image)

                        articleDiv.addView(recommendationItem)
                    }
                } else {
                    val articleNotFound = TextView(context).apply {
                        text = context.getString(R.string.article_not_found)
                        textSize = 16f
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    }
                    articleDiv.addView(articleNotFound)
                }
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Tidak perlu inflate menu, kita hanya ingin tangani tombol back
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    android.R.id.home -> {
                        findNavController().navigateUp()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun getColorFromAttr(context: Context, attr: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }

//    override fun onResume() {
//        super.onResume()
//        val activity = requireActivity() as AppCompatActivity
//
//        val actionBarColor = getColorFromAttr(activity, R.attr.colorPrimaryTool)
//        val titleColor = getColorFromAttr(activity, R.attr.colorAccent)
//
//        activity.supportActionBar?.apply {
//            setBackgroundDrawable(ColorDrawable(actionBarColor))
//            title = Html.fromHtml("<font color='${String.format("#%06X", 0xFFFFFF and titleColor)}'>DentAssist</font>", 1)
//        }
//    }
    override fun onResume() {
        super.onResume()
        val activity = requireActivity() as AppCompatActivity

        val actionBarColor = getColorFromAttr(activity, R.attr.colorPrimaryTool)
        val titleColor = getColorFromAttr(activity, R.attr.colorAccent)

        activity.supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(actionBarColor))
            title = Html.fromHtml(
                "<font color='${String.format("#%06X", 0xFFFFFF and titleColor)}'>Hasil Prediksi</font>",
                Html.FROM_HTML_MODE_LEGACY
            )
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        setHasOptionsMenu(true)

    val menuHost: MenuHost = requireActivity()
    menuHost.addMenuProvider(object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                android.R.id.home -> {
                    findNavController().navigateUp()
                    true
                }
                else -> false
            }
        }
    }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}