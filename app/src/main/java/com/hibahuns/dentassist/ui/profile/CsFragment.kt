package com.hibahuns.dentassist.ui.profile

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hibahuns.dentassist.R
import com.hibahuns.dentassist.databinding.FragmentCsBinding

/**
 * A simple [Fragment] subclass.
 * Use the [CsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CsFragment : Fragment() {
    private var _binding: FragmentCsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.whatsappButton.setOnClickListener {
            val url = "https://api.whatsapp.com/send/?phone=6282138449930&text&type=phone_number&app_absent=0"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            requireContext().startActivity(intent)
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
            title = Html.fromHtml("<font color='#EA7676'>Profile</font>", 1)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)
        }
    }

}