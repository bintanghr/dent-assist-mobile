package com.hibahuns.dentassist.data.api.request

import android.net.Uri

data class UpdateUserRequest(
    val username: String,
    val email: String,
    val city: String,
    val profileImage: String
)
