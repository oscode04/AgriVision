package com.agrivision.ui.weather

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agrivision.R
import com.agrivision.data.response.RamalanItem
import com.agrivision.databinding.ItemWeatherBinding
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private val weatherList = ArrayList<RamalanItem>()

    fun setWeatherList(list: List<RamalanItem>) {
        weatherList.clear()
        weatherList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = ItemWeatherBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(weatherList[position])
    }

    override fun getItemCount(): Int = weatherList.size

    inner class WeatherViewHolder(private val binding: ItemWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RamalanItem) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val waktuParsed = LocalTime.parse(item.waktu, formatter)
                val waktuMalam = LocalTime.of(17, 0)
                val waktuPagi = LocalTime.of(6, 0)

                Log.d(
                    "WeatherAdapter",
                    "Binding item: ${item.tanggal}, ${item.waktu}, ${item.suhu}, ${item.deskripsi}, ${item.kelembapan}"
                )
                binding.tvDate.text = item.tanggal
                binding.tvTime.text = item.waktu
                binding.tvTemperature.text = item.suhu
                when (item.deskripsi) {
                    "light rain" -> binding.tvDescription.text = "Hujan Ringan"
                    "overcast clouds" -> binding.tvDescription.text = "Mendung"
                    "moderate rain" -> binding.tvDescription.text = "Hujan Lebat"
                    "broken clouds" -> binding.tvDescription.text = "Berawan"
                    else -> binding.tvDescription.text = "Cerah"

                }
                binding.tvHumidity.text = item.kelembapan

                if ((waktuParsed.isBefore(waktuPagi) || waktuParsed.isAfter(waktuMalam))) {
                    when (item.deskripsi) {
                        "light rain" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_rain)
                        "overcast clouds" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_cloudy)
                        "moderate rain" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_storm)
                        "broken clouds" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_night_cloud)
                        else -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_clear_night)
                    }
                } else {
                    when (item.deskripsi) {
                        "light rain" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_rain)
                        "overcast clouds" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_cloudy)
                        "moderate rain" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_storm)
                        "broken clouds" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_morning_cloud)
                        else -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_clear)
                    }
                }
            } else {
                val calendar = Calendar.getInstance()
                val time =
                    item.waktu.split(" ")[1]
                val timeParts = time.split(":")
                val hour = timeParts[0].toInt()
                val minute = timeParts[1].toInt()

                val waktuMalam = 17
                val waktuPagi = 6

                Log.d(
                    "WeatherAdapter",
                    "Binding item (pre-O): ${item.tanggal}, ${item.waktu}, ${item.suhu}, ${item.deskripsi}, ${item.kelembapan}"
                )
                binding.tvDate.text = item.tanggal
                binding.tvTime.text = item.waktu
                binding.tvTemperature.text = item.suhu
                when (item.deskripsi) {
                    "light rain" -> binding.tvDescription.text = "Hujan Ringan"
                    "overcast clouds" -> binding.tvDescription.text = "Mendung"
                    "moderate rain" -> binding.tvDescription.text = "Hujan Lebat"
                    "broken clouds" -> binding.tvDescription.text = "Berawan"
                    else -> binding.tvDescription.text = "Cerah"
                }
                binding.tvHumidity.text = item.kelembapan

                if ((hour < waktuPagi || hour >= waktuMalam)) {
                    when (item.deskripsi) {
                        "light rain" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_rain)
                        "overcast clouds" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_cloudy)
                        "moderate rain" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_storm)
                        "broken clouds" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_night_cloud)
                        else -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_clear_night)
                    }
                } else {
                    when (item.deskripsi) {
                        "light rain" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_rain)
                        "overcast clouds" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_cloudy)
                        "moderate rain" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_storm)
                        "broken clouds" -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_morning_cloud)
                        else -> binding.ivWeatherIcon.setImageResource(R.drawable.ic_clear)
                    }
                }
            }
        }
    }
}