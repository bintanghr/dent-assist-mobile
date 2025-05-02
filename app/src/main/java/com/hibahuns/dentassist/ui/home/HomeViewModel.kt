package com.hibahuns.dentassist.ui.home

import android.content.Intent
import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hibahuns.dentassist.data.Repository
import com.hibahuns.dentassist.data.api.response.ArticleResponse
import com.hibahuns.dentassist.data.api.response.ClinicResponse
import com.hibahuns.dentassist.data.api.response.ProductResponse
import com.hibahuns.dentassist.data.api.response.UserResponse
import com.hibahuns.dentassist.data.pref.UserModel
import com.hibahuns.dentassist.ui.login.LoginActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {

    private val _clinics = MutableStateFlow<ClinicResponse?>(null)
    val clinics: StateFlow<ClinicResponse?> = _clinics

    private val _products = MutableStateFlow<ProductResponse?>(null)
    val products: StateFlow<ProductResponse?> = _products

    private val _articles = MutableStateFlow<ArticleResponse?>(null)
    val articles: StateFlow<ArticleResponse?> = _articles

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

    fun fetchArticles() {
        viewModelScope.launch {
            try {
                val response = repository.getArticles()
                _articles.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}