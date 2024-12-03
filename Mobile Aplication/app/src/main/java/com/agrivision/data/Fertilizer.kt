package com.agrivision.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FertilizerData(
    val N: String,
    val P: String,
    val K: String,
    val temperature: String,
    val humidity: String,
    val soil_type: String,
    val crop_type: String
) : Parcelable

