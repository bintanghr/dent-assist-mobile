package com.hibahuns.dentassist.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hibahuns.dentassist.data.Repository
import com.hibahuns.dentassist.data.api.response.HistoryResponse
import kotlinx.coroutines.launch

class NotificationsViewModel(private val repository: Repository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    fun getHistory(idUser: String): LiveData<HistoryResponse> {
        val historyData = MutableLiveData<HistoryResponse>()
        viewModelScope.launch {
            val response = repository.getHistory(idUser)
            historyData.postValue(response)

        }
        return historyData
    }
}