package com.agrivision.data.companion

data class Riwayat(
    val tanggal: String,
    val namaTanaman: String,
    val nitrogen: String,
    val fosfor: String,
    val kalium: String,
    val temperatur: String,
    val kelembapan: String,
    val rekomendasiPupuk: String,
    val gambarPupuk: Int
)
