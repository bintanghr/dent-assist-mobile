package com.hibahuns.dentassist.ui.clinics

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hibahuns.dentassist.databinding.FragmentClinicsBinding
import com.hibahuns.dentassist.ui.ViewModelFactory
import com.hibahuns.dentassist.ui.home.RvDataItem


@Suppress("DEPRECATION")
class ClinicsFragment : Fragment() {
    private var _binding: FragmentClinicsBinding? = null
    private val binding get() = _binding!!
    private var data: MutableList<RvDataItem>? = mutableListOf()
    private val clinicsViewModel: ClinicsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArrayList("datas", RvDataItem::class.java)
        } else {
            arguments?.getParcelable("datas")
        }

        data?.let { clinicsViewModel.setClinicsData(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClinicsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val clinicAdapter = ClinicAdapter()
        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = clinicAdapter
        }

        clinicsViewModel.filteredData.observe(viewLifecycleOwner) {
            clinicAdapter.submitList(it)
        }

        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clinicsViewModel.filterHistory(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return root
    }
}