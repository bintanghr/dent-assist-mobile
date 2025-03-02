package com.hibahuns.dentassist.ui.home

import android.icu.util.Calendar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hibahuns.dentassist.data.Repository
import com.hibahuns.dentassist.data.api.response.ClinicResponse
import com.hibahuns.dentassist.data.api.response.ProductResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private val _welcomeText = MutableLiveData<String>().apply {
        value = getGreetingMessage()
    }
    val welcomeText: LiveData<String> = _welcomeText

    private val _clinics = MutableStateFlow<ClinicResponse?>(null)
    val clinics: StateFlow<ClinicResponse?> = _clinics

    private val _products = MutableStateFlow<ProductResponse?>(null)
    val products: StateFlow<ProductResponse?> = _products

    fun fetchClinics() {
        viewModelScope.launch {
            try {
                val response = repository.getClinics()
                _clinics.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                val response = repository.getProducts()
                _products.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getGreetingMessage(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 5..11 -> "Selamat Pagi, Teuku Umar!"
            in 12..14 -> "Selamat Siang, Teuku Umar!"
            in 15..17 -> "Selamat Sore, Teuku Umar!"
            else -> "Selamat Malam, Teuku Umar!"
        }
    }
}