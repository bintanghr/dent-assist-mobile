package com.hibahuns.dentassist.ui.clinics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hibahuns.dentassist.data.Repository
import com.hibahuns.dentassist.ui.home.RvDataItem

class ClinicsViewModel : ViewModel() {

    private val _data = MutableLiveData<List<RvDataItem>>()
    private val _filteredData = MutableLiveData<List<RvDataItem>>()
    val filteredData: LiveData<List<RvDataItem>> get() = _filteredData

    fun filterHistory(query: String) {
        val data = _data.value ?: return
        if (query.isEmpty()) {
            _filteredData.value = data
        } else {
            _filteredData.value = data.filter { it.title.contains(query, ignoreCase = true) }
        }
    }

    fun setClinicsData(data: MutableList<RvDataItem>) {
        _data.value = data
        _filteredData.value = data
    }
}