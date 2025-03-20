package com.hibahuns.dentassist.ui.clinics

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

data class ClinicDataItem (
    val title: String,
    val description: String,
    val imageUrl: String,
)  : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ClinicDataItem> {
        override fun createFromParcel(parcel: Parcel): ClinicDataItem {
            return ClinicDataItem(parcel)
        }

        override fun newArray(size: Int): Array<ClinicDataItem?> {
            return arrayOfNulls(size)
        }
    }
}