package com.hibahuns.dentassist.data.api.response

import com.google.gson.annotations.SerializedName

data class ArticleResponse(

	@field:SerializedName("data")
	val data: List<ProductDataItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ArticleDataItem(
	@field:SerializedName("idArticle")
	val idArticle: String? = null,

	@field:SerializedName("disease")
	val disease: String? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("title")
	val title: String? = null,
)
