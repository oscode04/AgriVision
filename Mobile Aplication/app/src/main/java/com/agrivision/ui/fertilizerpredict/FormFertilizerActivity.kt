package com.agrivision.ui.fertilizerpredict

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.agrivision.R
import com.agrivision.data.FertilizerData
import com.agrivision.data.response.FertilizerPrediction
import com.agrivision.data.retrofit.ApiConfig
import com.agrivision.data.retrofit.ApiConfigML
import com.agrivision.data.retrofit.ApiService
import com.agrivision.databinding.ActivityFormFertilizerBinding
import kotlinx.coroutines.launch

class FormFertilizerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormFertilizerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFormFertilizerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.filledButton.setOnClickListener { predictFertilizer() }
        }

    private fun predictFertilizer(){
        lifecycleScope.launch {
            binding.tvPredict.visibility = View.GONE
            binding.progressBar2.visibility = View.VISIBLE
            try {
                val api = ApiConfigML.getApiService()
                val responseToken = api.getToken()
                val token = responseToken.body()?.accessToken
                Log.d("cek token", token.toString())
                if (token != null){

                    val nitrogen = binding.inputTextNitrogen.text.toString()
                    val fosfor = binding.inputTextFosfor.text.toString()
                    val kalium = binding.inputTextKalium.text.toString()
                    val temperature = binding.inputTextTemperature.text.toString()
                    val kelembapan = binding.inputTextKelembapan.text.toString()
                    val jenisTanah : String
                    when (binding.jenisTanah.text.toString()) {
                        "Tanah Pasir" -> jenisTanah = ("Sandy")
                        "Tanah Lempung Berpasir" -> jenisTanah = ("Loamy")
                        "Tanah Hitam" -> jenisTanah =("Black")
                        "Tanah Merah" -> jenisTanah =("Red")
                        "Tanah Lempung" -> jenisTanah =("Clayey")
                        else -> jenisTanah =("Jenis tanah tidak diketahui")

                    }
                    val tanaman : String
                    when (binding.dropdownMenuPlantType.text.toString()) {
                        "Jagung" -> tanaman = ("Maize")
                        "Tebu" -> tanaman = ("Sugarcane")
                        "Kapas" -> tanaman = ("Cotton")
                        "Tembakau" -> tanaman = ("Tobacco")
                        "Padi" -> tanaman = ("Paddy")
                        "Barli" -> tanaman = ("Barley")
                        "Gandum" -> tanaman = ("Wheat")
                        "Millets" -> tanaman = ("Millets")
                        "Biji Minyak" -> tanaman = ("Oil Seeds")
                        "Kacang-kacangan" -> tanaman = ("Pulses")
                        "Kacang Tanah" -> tanaman = ("Ground Nuts")
                        "Beras" -> tanaman = ("Rice")
                        "Delima" -> tanaman = ("Pomegranate")
                        "Kopi" -> tanaman = ("Coffee")
                        "Semangka" -> tanaman = ("Watermelon")
                        "Kacang Merah" -> tanaman = ("Kidneybeans")
                        "Jeruk" -> tanaman = ("Orange")
                        else -> tanaman = ("Tanaman Tidak Dikenal")
                    }
                    val requiredData = FertilizerData(nitrogen, fosfor, kalium, temperature, kelembapan, jenisTanah, tanaman)

                    val response = api.getFertilizerPred("Bearer $token",requiredData)
                    if (response.isSuccessful){
                        when(response.body()?.predictedFertilizer) {
                            "Urea" -> binding.tvPredict.text =
                                getString(R.string.urea_recommendation)

                            "DAP" -> binding.tvPredict.text = getString(R.string.dap_recommendation)
                            "14-35-14" -> binding.tvPredict.text =
                                getString(R.string.R14_35_14_recommendation)

                            "28-28" -> binding.tvPredict.text =
                                getString(R.string.R28_28_recommendation)

                            "17-17-17" -> binding.tvPredict.text =
                                getString(R.string.R17_17_17_recommendation)

                            "20-20" -> binding.tvPredict.text =
                                getString(R.string.R20_20_recommendation)

                            "10-26-26" -> binding.tvPredict.text =
                                getString(R.string.R10_26_26_recommendation)

                            "TSP" -> binding.tvPredict.text = getString(R.string.tsp_recommendation)
                            "Superphosphate" -> binding.tvPredict.text =
                                getString(R.string.superphosphate_recommendation)

                            "Potassium Sulfate" -> binding.tvPredict.text =
                                getString(R.string.potassium_sulfate_recommendation)

                            "Potassium Chloride" -> binding.tvPredict.text =
                                getString(R.string.potassium_chloride_recommendation)

                            "15-15-15" -> binding.tvPredict.text =
                                getString(R.string.R15_15_15_recommendation)

                            "10-10-10" -> binding.tvPredict.text =
                                getString(R.string.R10_10_10_recommendation)

                            "14-14-14" -> binding.tvPredict.text =
                                getString(R.string.R14_14_14_recommendation)

                            else -> binding.tvPredict.text =
                                getString(R.string.default_recommendation)
                        }


                    } else {
                        Log.e("post fertilizer", response.message())
                    }
                } else {
                    Toast.makeText(this@FormFertilizerActivity,responseToken.message(),Toast.LENGTH_SHORT).show()
                }
            }
            catch (e:Exception){
            Toast.makeText(this@FormFertilizerActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.tvPredict.visibility = View.VISIBLE
                binding.progressBar2.visibility = View.GONE
        }
    }
    }
}

