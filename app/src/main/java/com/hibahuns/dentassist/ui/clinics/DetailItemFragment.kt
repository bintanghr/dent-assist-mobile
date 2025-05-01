package com.hibahuns.dentassist.ui.clinics

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                description.text = data.description
                subDescription.text = data.subDescription

                val (descTitle, subDescTitle) = when (data.type) {
                    "clinic" -> Pair("Alamat Lengkap", "Rating")
                    "product" -> Pair("Penyakit", "Dosis")
                    "article" -> Pair("", "")
                    else -> Pair("Deskripsi", "Deskripsi")
                }

                descriptionTitle.text = descTitle
                subDescriptionTitle.text = subDescTitle

                Glide.with(requireContext())
                    .load(data.imageUrl)
                    .placeholder(R.drawable.image_preview)
                    .error(R.drawable.image_preview)
                    .into(itemImg)
            }
        }

        return root
    }
}