package com.hibahuns.dentassist.data.api.response

import com.google.gson.annotations.SerializedName

data class ProductResponse(

	@field:SerializedName("data")
	val data: List<ProductDataItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ProductDataItem(

	@field:SerializedName("idProduct")
	val idProduct: String? = null,

	@field:SerializedName("disease")
	val disease: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("dosis")
	val dosis: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("ket")
	val ket: String? = null,

	@field:SerializedName("link_photo")
	val linkPhoto: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("shape")
	val shape: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("notes")
	val notes: List<String>? = null,

	@field:SerializedName("keys")
	val keys: List<String>? = null
)
