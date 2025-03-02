package com.hibahuns.dentassist.data

import com.hibahuns.dentassist.data.api.request.LoginRequest
import com.hibahuns.dentassist.data.api.request.SignupRequest
import com.hibahuns.dentassist.data.api.request.UpdateUserRequest
import com.hibahuns.dentassist.data.api.response.ClinicResponse
import com.hibahuns.dentassist.data.api.response.HistoryResponse
import com.hibahuns.dentassist.data.api.response.LoginResponse
import com.hibahuns.dentassist.data.api.response.PredictResponse
import com.hibahuns.dentassist.data.api.response.ProductResponse
import com.hibahuns.dentassist.data.api.response.SignupResponse
import com.hibahuns.dentassist.data.api.response.UserResponse
import com.hibahuns.dentassist.data.api.retrofit.ApiService
import com.hibahuns.dentassist.data.pref.UserModel
import com.hibahuns.dentassist.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Repository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun signup(username: String, email: String, password: String, city: String): SignupResponse {
        val request = SignupRequest(username, email, password, city)
        return apiService.signup(request)
    }
    suspend fun login(email: String, password: String): LoginResponse {
        val request = LoginRequest(email, password)
        return apiService.login(request)
    }

    suspend fun getUserProfile(idUser: String): UserResponse {
        return apiService.user(idUser)
    }

    suspend fun updateUserProfile(
        idUser: String,
        username: String,
        email: String,
        city: String,
    ): UserResponse {
        val request = UpdateUserRequest(username, email,city)
        return apiService.updateUserProfile(idUser, request)
    }


    suspend fun predict(imageFile: MultipartBody.Part, idUser: RequestBody): PredictResponse {
        return apiService.predict(imageFile, idUser)
    }

    suspend fun getClinics(): ClinicResponse {
        return apiService.getClinics()
    }

    suspend fun getProducts(): ProductResponse {
        return apiService.getProducts()
    }

    suspend fun getHistory(idUser: String): HistoryResponse {
        return apiService.getHistories(idUser)
    }
    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(userPreference, apiService)
            }.also { instance = it }
    }
}