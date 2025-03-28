package com.hibahuns.dentassist.ui.prediction

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.hibahuns.dentassist.R
import com.hibahuns.dentassist.data.api.response.PredictionData
import com.hibahuns.dentassist.databinding.FragmentPredictionBinding
import com.hibahuns.dentassist.ui.ViewModelFactory

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
                diseaseText.text = data.label
                description.text = data.explanation
                suggestion.text = data.suggestion

                Glide.with(requireContext())
                    .load(data.signedUrl)
                    .placeholder(R.drawable.image_preview)
                    .error(R.drawable.image_preview)
                    .into(submitedPhoto)
            }
        }

        return root
    }

    fun getColorFromAttr(context: Context, attr: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
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

}