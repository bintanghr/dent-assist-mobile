package com.hibahuns.dentassist.ui.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RvDataItem (
    val title: String,
    val description: String,
    val imageUrl: String,
) : Parcelable