package com.hibahuns.dentassist.data.api.request

import android.net.Uri
import okhttp3.MultipartBody

data class UpdateUserRequest(
    val username: String,
    val email: String,
    val password: String,
    val city: String,
    val profileImage: MultipartBody.Part?
)
