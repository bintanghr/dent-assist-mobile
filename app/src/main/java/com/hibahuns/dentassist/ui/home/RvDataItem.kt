package com.hibahuns.dentassist.ui.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RvDataItem (
    val id: String,
    val title: String,
    val subTitle: String,
    val category: String,
    val category2: String,
    val description: String,
    val subDescription: String,
    val subDescription2: List<String>,
    val keys: List<String>,
    val redirectUrl: String,
    val imageUrl: String,
    val type: String
) : Parcelable