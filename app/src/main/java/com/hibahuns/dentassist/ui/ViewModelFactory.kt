package com.hibahuns.dentassist.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hibahuns.dentassist.data.Repository
import com.hibahuns.dentassist.di.Injection
import com.hibahuns.dentassist.ui.dashboard.DashboardViewModel
import com.hibahuns.dentassist.ui.home.HomeViewModel
import com.hibahuns.dentassist.ui.login.LoginViewModel
import com.hibahuns.dentassist.ui.notifications.NotificationsViewModel
import com.hibahuns.dentassist.ui.prediction.PredictionViewModel
import com.hibahuns.dentassist.ui.profile.ProfileViewModel
import com.hibahuns.dentassist.ui.signup.SignupViewModel

class ViewModelFactory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(NotificationsViewModel::class.java) -> {
                NotificationsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PredictionViewModel::class.java) -> {
                PredictionViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
//            modelClass.isAssignableFrom(CameraViewModel::class.java) -> {
//                CameraViewModel(repository) as T
//            }
//            modelClass.isAssignableFrom(ClinikViewModel::class.java) -> {
//                ClinikViewModel(repository) as T
//            }
//            modelClass.isAssignableFrom(ProductViewModel::class.java) -> {
//                ProductViewModel(repository) as T
//            }
//            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
//                HistoryViewModel(repository) as T
//            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}