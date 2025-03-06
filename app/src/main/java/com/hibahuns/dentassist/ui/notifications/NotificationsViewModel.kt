package com.hibahuns.dentassist.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hibahuns.dentassist.data.Repository
import com.hibahuns.dentassist.data.api.response.DataItemHistory
import kotlinx.coroutines.launch

class NotificationsViewModel(private val repository: Repository) : ViewModel() {

    private val _historiesData = MutableLiveData<List<DataItemHistory>>()
    private val _filteredHistories = MutableLiveData<List<DataItemHistory>>()
    val filteredHistories: LiveData<List<DataItemHistory>> get() = _filteredHistories

    fun getHistories(userId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getHistory(userId)
                _historiesData.value = response.data
                _filteredHistories.value = response.data
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun filterHistory(query: String) {
        val data = _historiesData.value ?: return
        if (query.isEmpty()) {
            _filteredHistories.value = data
        } else {
            _filteredHistories.value = data.filter { it.label.contains(query, ignoreCase = true) }
        }
    }
}