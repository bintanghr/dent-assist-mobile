package com.hibahuns.dentassist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hibahuns.dentassist.ui.setting.SettingPreferences
import com.hibahuns.dentassist.ui.setting.SgViewModel

class ViewModelFactorySg(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SgViewModel::class.java)) {
            return SgViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}