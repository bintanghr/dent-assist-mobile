package com.hibahuns.dentassist.ui.profile
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.hibahuns.dentassist.data.Repository
import com.hibahuns.dentassist.data.api.response.ClinicResponse
import com.hibahuns.dentassist.data.api.response.UserResponse
import com.hibahuns.dentassist.data.pref.UserModel
import com.hibahuns.dentassist.data.pref.UserPreference
import com.hibahuns.dentassist.data.pref.dataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class ProfileViewModel(private val repository: Repository) : ViewModel() {

    private val _userData = MutableLiveData<UserResponse?>()
    val userData: LiveData<UserResponse?> get() = _userData

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun fetchData(userId: String) {
        viewModelScope.launch {
            try {
                _userData.value = repository.getUserProfile(userId)
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateUserProfile(
        userId: String,
        username: RequestBody,
        email: RequestBody,
        password: RequestBody?,
        city: RequestBody,
        imageFile: MultipartBody.Part?) {

        viewModelScope.launch {
            try {
                val response = repository.updateUserProfile(
                    userId,
                    username,
                    email,
                    password,
                    city,
                    imageFile
                )
                Log.d("updateUserProfile response", "$response")
            } catch(e: Exception) {
                Log.e("Error on updateUserProfile", "$e")
            }

        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}
