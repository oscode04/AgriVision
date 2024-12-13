package com.agrivision.data.companion

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

@Parcelize
data class message(
    val message: String
) : Parcelable
@Parcelize
data class LoginRequest(
    val emailOrUsername: String,
    val password: String
) : Parcelable
@Parcelize
data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String,
    val nickname: String
): Parcelable
@Parcelize
data class VerifyRequest(
    val username: String?,
    val verificationCode: String
): Parcelable

