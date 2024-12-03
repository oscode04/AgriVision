package com.agrivision.utils
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.core.content.ContextCompat
import com.agrivision.data.response.CuacaItem
import com.agrivision.data.response.RamalanItem
import com.agrivision.data.retrofit.ApiConfig
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

fun fetchCurrentLocation(context: Context): Location? {
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
        val locationTask = fusedLocationClient.lastLocation
        return try {
            locationTask.result
        } catch (e: Exception) {
            null
        }
    } else {
        throw SecurityException("Izin lokasi belum diberikan.")
    }
}
suspend fun fetchWeatherForecast(
    latitude: Double,
    longitude: Double
): Pair<String, List<RamalanItem>> = withContext(Dispatchers.IO) {
    try {
        val response = ApiConfig.getApiService().getWeatherForecast(latitude, longitude)
        if (response.isSuccessful) {
            val weatherData = response.body()
            val cityName = weatherData?.kota ?: "No Info"
            val ramalanList = weatherData?.ramalan?.flatMap { it.ramalan } ?: emptyList()
            return@withContext cityName to ramalanList
        } else {
            throw Exception("Gagal memuat data: ${response.message()}")
        }
    } catch (e: Exception) {
        throw Exception("Terjadi kesalahan: ${e.message}")
    }
}


suspend fun fetchLatestWeatherForecast(
    latitude: Double,
    longitude: Double
): CuacaItem = withContext(Dispatchers.IO) {
    val response = ApiConfig.getApiService().getWeatherForecast(latitude, longitude)

    val cityName = response.body()?.kota ?: "Tidak diketahui"


    val currentDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDateTime.now()
    } else {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        val dateTime = String.format(
            Locale.ROOT, "%d-%02d-%02d %02d:%02d:%02d",
            year, month, day, hour, minute, second
        )
        dateTime
    }

    val currentDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        (currentDateTime as LocalDateTime).toLocalDate().toString()
    } else {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        String.format(Locale.ROOT, "%d-%02d-%02d", year, month, day)
    }

    val currentTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        (currentDateTime as LocalDateTime).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    } else {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        String.format(Locale.ROOT, "%d-%02d-%02d %02d:%02d:%02d",
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH),
            hour,
            minute,
            second)
    }
    val todayForecasts = response.body()?.ramalan
        ?.firstOrNull { it.tanggal == currentDate }
        ?.ramalan ?: emptyList()


    val closestForecast = todayForecasts
        .filter { it.waktu <= currentTime }
        .maxByOrNull { it.waktu }

    val temperature = closestForecast?.suhu ?: "?"
    val description = closestForecast?.deskripsi ?: "Deskripsi tidak tersedia"
    val humidity = closestForecast?.kelembapan ?: "Kelembapan tidak tersedia"

    CuacaItem(cityName, "$temperature", description, humidity)
}