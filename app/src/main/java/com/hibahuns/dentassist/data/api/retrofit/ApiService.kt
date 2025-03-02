package com.hibahuns.dentassist.data.api.retrofit

import com.hibahuns.dentassist.data.api.request.LoginRequest
import com.hibahuns.dentassist.data.api.request.SignupRequest
import com.hibahuns.dentassist.data.api.request.UpdateUserRequest
import com.hibahuns.dentassist.data.api.response.ArticleResponse
import com.hibahuns.dentassist.data.api.response.ClinicResponse
import com.hibahuns.dentassist.data.api.response.HistoryResponse
import com.hibahuns.dentassist.data.api.response.LoginResponse
import com.hibahuns.dentassist.data.api.response.PredictResponse
import com.hibahuns.dentassist.data.api.response.ProductResponse
import com.hibahuns.dentassist.data.api.response.SignupResponse
import com.hibahuns.dentassist.data.api.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("auth/signup")
    suspend fun signup(
        @Body request: SignupRequest
    ): SignupResponse

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @GET("users/{idUser}")
    suspend fun user(
        @Path("idUser") idUser: String): UserResponse

    @PUT("users/{idUser}")
    suspend fun updateUserProfile(
        @Path("idUser") idUser: String,
        @Body request: UpdateUserRequest
    ): UserResponse

    @Multipart
    @POST("predict")
    suspend fun predict(
        @Part image: MultipartBody.Part,
        @Part("idUser") idUser: RequestBody
    ): PredictResponse

    @GET("users/{idUser}/histories")
    suspend fun getHistories(
        @Path("idUser") idUser: String
    ): HistoryResponse


    @GET("clinics")
    suspend fun getClinics(): ClinicResponse


    @GET("products")
    suspend fun getProducts(): ProductResponse

    @GET("articles")
    suspend fun getArticles(): ArticleResponse

    @POST("logout")
    suspend fun logout(): SignupResponse
}

