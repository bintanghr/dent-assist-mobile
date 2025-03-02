package com.hibahuns.dentassist.di

import android.content.Context
import com.hibahuns.dentassist.data.Repository
import com.hibahuns.dentassist.data.api.retrofit.ApiConfig
import com.hibahuns.dentassist.data.pref.UserPreference
import com.hibahuns.dentassist.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.idUser)
        return Repository.getInstance(pref, apiService)
    }
}