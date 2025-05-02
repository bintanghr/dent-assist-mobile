package com.hibahuns.dentassist.ui.clinics

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.hibahuns.dentassist.R
import com.hibahuns.dentassist.data.api.response.PredictionData
import com.hibahuns.dentassist.databinding.FragmentDetailItemBinding
import com.hibahuns.dentassist.databinding.FragmentPredictionBinding
import com.hibahuns.dentassist.ui.home.RvDataItem

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailItemFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailItemFragment : Fragment() {
    private var _binding: FragmentDetailItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailItemBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val itemData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("itemData", RvDataItem::class.java)
        } else {
            arguments?.getParcelable("itemData")
        }

        itemData?.let { data ->
            with(binding) {
                title.text = data.title
                subTitle.text = data.subTitle
                category.text = data.category
                category2.text = data.category2
                description.text = data.description
                subDescription.text = data.subDescription

                for (sentence in data.subDescription2) {
                    val newTextView = TextView(context).apply {
                        id = View.generateViewId()
                        text = "\u2022 $sentence"
                        setTextColor(ContextCompat.getColor(context, R.color.font_grey))
                        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                        typeface = ResourcesCompat.getFont(context, R.font.poppins_regular)
                        layoutParams = ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                        )
                    }

                    binding.subDescription2Div.addView(newTextView)
                }

                when (data.type) {
                    "clinic" -> {
                        categoryTitle.text = "Nomor Telepon"
                        descriptionTitle.text = "Alamat Lengkap"
                        subDescriptionTitle.text = "Rating"
                        subDescriptionTitle2.text = ""
                        redirectButton.text = "Buka Google maps"
                        redirectButton.setOnClickListener {
                            val url = data.redirectUrl
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            requireContext().startActivity(intent)
                        }
                    }
                    "product" -> {
                        categoryTitle2.text = "Bentuk Obat"
                        descriptionTitle.text = "Deskripsi"
                        subDescriptionTitle.text = "Dosis"
                        subDescriptionTitle2.text = "Hal yang perlu diperhatikan"
                        redirectButton.visibility = View.GONE
                    }
                    "article" -> {
                        categoryTitle.text = "Tanggal Penerbitan"
                        categoryTitle2.visibility = View.GONE
                        descriptionTitle.text = "Abstrak"
                        subDescriptionTitle.text = ""
                        subDescriptionTitle2.text = ""
                        redirectButton.text = "Buka website artikel"
                        redirectButton.setOnClickListener {
                            val url = data.redirectUrl
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            requireContext().startActivity(intent)
                        }
                    }
                    else -> {
                        categoryTitle2.text = ""
                        descriptionTitle.text = ""
                        subDescriptionTitle.text = ""
                        subDescriptionTitle2.text = ""
                    }
                }

                Glide.with(requireContext())
                    .load(data.imageUrl)
                    .placeholder(R.drawable.image_preview)
                    .error(R.drawable.image_preview)
                    .into(itemImg)
            }
        }

        return root
    }
    fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}