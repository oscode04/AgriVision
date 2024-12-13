package com.agrivision.ui.riwayat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.agrivision.R
import com.agrivision.data.companion.Riwayat

class RiwayatAdapter(private val listRiwayat: List<Riwayat>) :
    RecyclerView.Adapter<RiwayatAdapter.RiwayatViewHolder>() {

    class RiwayatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val tvDetails: TextView = itemView.findViewById(R.id.tv_details)
        val tvRecommendation: TextView = itemView.findViewById(R.id.tv_recommendation)
        val imgFertilizer: ImageView = itemView.findViewById(R.id.img_fertilizer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat, parent, false)
        return RiwayatViewHolder(view)
    }

    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
        val riwayat = listRiwayat[position]
        holder.tvDate.text = "Tanggal Riwayat: ${riwayat.tanggal}"
        holder.tvDetails.text = """
            • Nama Tanaman: ${riwayat.namaTanaman}
            • Kandungan Nitrogen: ${riwayat.nitrogen}
            • Kandungan Fosfor: ${riwayat.fosfor}
            • Kandungan Kalium: ${riwayat.kalium}
            • Temperatur: ${riwayat.temperatur}
            • Kelembapan: ${riwayat.kelembapan}
        """.trimIndent()
        holder.tvRecommendation.text = "Rekomendasi Pupuk: ${riwayat.rekomendasiPupuk}"
        holder.imgFertilizer.setImageResource(riwayat.gambarPupuk)
    }

    override fun getItemCount(): Int = listRiwayat.size
}
