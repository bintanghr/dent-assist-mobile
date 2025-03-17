package com.hibahuns.dentassist.ui.customer_support

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hibahuns.dentassist.R
import com.hibahuns.dentassist.databinding.FragmentCsBinding
import com.hibahuns.dentassist.databinding.FragmentNotificationsBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
            val url = "wa.me/6281391561584"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        return root
    }

}