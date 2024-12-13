package com.agrivision.ui.weather

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.Manifest
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.agrivision.R
import com.agrivision.data.remote.response.RamalanItem
import com.agrivision.data.remote.response.ResponseCCWeather
import com.agrivision.data.remote.retrofit.ApiConfig
import com.agrivision.databinding.ActivityWeatherBinding
import com.agrivision.utils.fetchCurrentLocation
import com.agrivision.utils.fetchWeatherForecast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var binding: ActivityWeatherBinding
    private lateinit var weatherAdapter: WeatherAdapter
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                else -> {
                    Toast.makeText(this,"Anda belum mengizinkan lokasi",Toast.LENGTH_SHORT).show()
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvWeather.layoutManager = LinearLayoutManager(this)
        weatherAdapter = WeatherAdapter()
        binding.rvWeather.adapter = weatherAdapter
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getMyLastLocation()
    }


    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun getMyLastLocation() {
        if(checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null){
                    fetchWeatherData(location.latitude,location.longitude)
                } else {
                    Toast.makeText(this, "Gagal mendapatkan lokasi", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Gagal mendapatkan lokasi: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun fetchWeatherData(lat: Double, long: Double) {
        binding.progressBar3.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val (cityName, ramalanList) = fetchWeatherForecast(lat, long)
                binding.tvCityName.text = cityName
                weatherAdapter.setWeatherList(ramalanList)
            } catch (e: Exception) {
                Toast.makeText(this@WeatherActivity, "No internet", Toast.LENGTH_SHORT).show()
                Log.d("fetch weather", "Error: ${e.message}")
            } finally {
                binding.progressBar3.visibility = View.GONE
            }
        }
    }
}
