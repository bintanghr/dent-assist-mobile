package com.hibahuns.dentassist.ui.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RvDataItem (
    val id: String,
    val title: String,
    val subTitle: String,
    val category: String,
    val description: String,
    val subDescription: String,
    val imageUrl: String,
    val type: String
) : Parcelable