package com.hibahuns.dentassist.data.pref

data class UserModel(
    val email: String,
    val idUser: String,
    val isLogin: Boolean = false,
    val username: String,
    val city: String,
    val profileImage: String
)