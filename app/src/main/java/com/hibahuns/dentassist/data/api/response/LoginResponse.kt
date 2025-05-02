package com.hibahuns.dentassist.data.api.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("idUser")
	val idUser: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("profileImage")
	val profileImage: String? = null
)
