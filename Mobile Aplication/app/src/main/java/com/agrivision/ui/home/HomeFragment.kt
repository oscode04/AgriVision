package com.agrivision.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agrivision.R
import com.agrivision.data.local.DataStoreManager
import com.agrivision.data.remote.response.ArticleResponseItem
import com.agrivision.databinding.FragmentHomeBinding
import com.agrivision.ui.artikel.ArtikelViewModel
import com.agrivision.ui.fertilizerpredict.FormFertilizerActivity
import com.agrivision.ui.artikel.ListArticleAdapter
import com.agrivision.ui.chatbot.ChatActivity
import com.agrivision.ui.weather.WeatherActivity
import com.agrivision.utils.fetchLatestWeatherForecast
import com.google.android.gms.location.*
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var rvTrick: RecyclerView
    private val listArticleAdapter = ListArticleAdapter(arrayListOf())
    private lateinit var dataStoreManager : DataStoreManager
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                getMyLastLocation()
            }
            else -> {
                Toast.makeText(requireActivity(), "Anda belum mengizinkan lokasi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        dataStoreManager = DataStoreManager(requireActivity())
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val artikelViewModel =
            ViewModelProvider(this).get(ArtikelViewModel::class.java)

        getMyLastLocation()

        homeViewModel.text.observe(viewLifecycleOwner) { }

        binding.btnCuacaFailed.setOnClickListener { (getMyLastLocation()) }

        binding.btnPupuk.setOnClickListener {
            val intent = Intent(requireActivity(), FormFertilizerActivity::class.java)
            startActivity(intent)
        }

        binding.btnCuaca.setOnClickListener {
            val intent = Intent(requireActivity(), WeatherActivity::class.java)
            startActivity(intent)
        }

        binding.btnChat.setOnClickListener {
            val intent = Intent(requireActivity(), ChatActivity::class.java)
            startActivity(intent)
        }

        rvTrick = binding.rvTrick
        rvTrick.setHasFixedSize(true)
        rvTrick.layoutManager = LinearLayoutManager(requireActivity())
        rvTrick.adapter = listArticleAdapter
        binding.btnRetry.setOnClickListener {
            artikelViewModel.getData()
        }

        artikelViewModel.articlesItem.observe(viewLifecycleOwner){articleList ->
            setArticlesData(articleList)

        }

        artikelViewModel.isLoading.observe(viewLifecycleOwner) {
            loading(it)
        }

        artikelViewModel.isRetry.observe(viewLifecycleOwner) {
            retry(it)
        }


        artikelViewModel.isErr.observe(viewLifecycleOwner){
            if (it == "no error" || it.isNullOrEmpty()) {
                Log.d("fetch artikel","sukses coy")
            } else {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }


    fun loading(isLoading: Boolean) {
        if (isLoading != false) {
            binding.progressBar2.visibility = View.VISIBLE
        } else {
            binding.progressBar2.visibility = View.GONE
        }
    }
    private fun setArticlesData(dataEvents: List<ArticleResponseItem>) {
        listArticleAdapter.apply {
            listArticle.clear()
            listArticle.addAll(dataEvents.take(5))
            notifyDataSetChanged()
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(requireActivity(), permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {

        binding.llLoading.visibility = View.VISIBLE
        binding.llFailed.visibility = View.GONE
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {

            if (isGPSEnabled()) {

                val handler = Handler(Looper.getMainLooper())
                val timeoutRunnable = Runnable {
                    Toast.makeText(requireActivity(), "Gagal mendapatkan lokasi", Toast.LENGTH_SHORT).show()
                    binding.llLoading.visibility = View.GONE
                    binding.llFailed.visibility = View.VISIBLE
                }
                handler.postDelayed(timeoutRunnable, 3000)

                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    handler.removeCallbacks(timeoutRunnable)
                    if (location != null) {
                        getWeatherData(location.latitude, location.longitude)
                    } else {
                        requestLocationUpdate()
                    }
                }.addOnFailureListener {
                    handler.removeCallbacks(timeoutRunnable)
                    Toast.makeText(requireActivity(), "Gagal mendapatkan lokasi: ${it.message}", Toast.LENGTH_SHORT).show()
                    binding.llLoading.visibility = View.GONE
                    binding.llFailed.visibility = View.VISIBLE
                }
            } else {
                Toast.makeText(requireActivity(), "Gagal mendapatkan lokasi: GPS belum diaktifkan", Toast.LENGTH_SHORT).show()
                binding.llLoading.visibility = View.GONE
                binding.llFailed.visibility = View.VISIBLE
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }
    }

    private fun isGPSEnabled(): Boolean {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun requestLocationUpdate() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {

            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L).build()

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        val location = locationResult.lastLocation
                        if (location != null) {
                            getWeatherData(location.latitude, location.longitude)
                        } else {
                            Toast.makeText(requireActivity(), "Gagal mendapatkan lokasi. Pastikan GPS aktif.", Toast.LENGTH_SHORT).show()
                            binding.llLoading.visibility = View.GONE
                            binding.llFailed.visibility = View.VISIBLE
                        }
                        fusedLocationClient.removeLocationUpdates(this)
                    }
                },
                requireActivity().mainLooper
            )
        } else {
            requestPermissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }
    }

    fun getWeatherData(lat: Double, long: Double) {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                lifecycleScope.launch {
                    try {
                        val (cityName, temperature, description, humidity) = fetchLatestWeatherForecast(lat, long)

                        binding.tvCity.text = cityName
                        binding.tvTemperature.text = temperature
                        binding.tvDescription.text = when (description) {
                            "light rain" -> "Hujan Ringan"
                            "overcast clouds" -> "Mendung"
                            "moderate rain" -> "Hujan Lebat"
                            "broken clouds" -> "Berawan"
                            else -> "Cerah"
                        }
                        binding.tvHumidity.text = humidity

                        binding.ivWeatherIcon.setImageResource(when (description) {
                            "light rain" -> R.drawable.ic_rain_home
                            "overcast clouds" -> R.drawable.ic_cloudy_home
                            "moderate rain" -> R.drawable.ic_storm_home
                            "broken clouds" -> R.drawable.ic_morning_cloud
                            else -> R.drawable.ic_clear_home
                        })
                        binding.btnCuacaFailed.visibility = View.GONE
                        binding.llLoading.visibility = View.GONE

                        binding.btnCuaca.visibility = View.VISIBLE
                        binding.wtrData.visibility = View.VISIBLE


                    } catch (e: Exception) {
                        if (e.message != null) {
                            Toast.makeText(
                                requireActivity(),
                                "Error: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }
    }
    private fun retry(isRetry: Boolean) {
        if (isRetry != false) {
            binding.btnRetry.visibility = View.VISIBLE

        } else {
            binding.btnRetry.visibility = View.GONE
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
