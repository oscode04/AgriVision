package com.agrivision.ui.riwayat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.agrivision.R
import com.agrivision.data.companion.Riwayat
import com.agrivision.databinding.FragmentRiwayatBinding

class RiwayatFragment : Fragment() {

    private var _binding: FragmentRiwayatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRiwayatBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()

        return root
    }

    private fun setupRecyclerView() {
        val dummyData = listOf(
            Riwayat(
                tanggal = "01-01-2024",
                namaTanaman = "Padi",
                nitrogen = "2%",
                fosfor = "1%",
                kalium = "1.5%",
                temperatur = "25°C",
                kelembapan = "60%",
                rekomendasiPupuk = "Urea",
                gambarPupuk = R.drawable.ic_fertilizer_placeholder
            ),
            Riwayat(
                tanggal = "01-01-2024",
                namaTanaman = "Padi",
                nitrogen = "2%",
                fosfor = "1%",
                kalium = "1.5%",
                temperatur = "25°C",
                kelembapan = "60%",
                rekomendasiPupuk = "Urea",
                gambarPupuk = R.drawable.ic_fertilizer_placeholder
            ), Riwayat(
                tanggal = "01-01-2024",
                namaTanaman = "Padi",
                nitrogen = "2%",
                fosfor = "1%",
                kalium = "1.5%",
                temperatur = "25°C",
                kelembapan = "60%",
                rekomendasiPupuk = "Urea",
                gambarPupuk = R.drawable.ic_fertilizer_placeholder
            ), Riwayat(
                tanggal = "01-01-2024",
                namaTanaman = "Padi",
                nitrogen = "2%",
                fosfor = "1%",
                kalium = "1.5%",
                temperatur = "25°C",
                kelembapan = "60%",
                rekomendasiPupuk = "Urea",
                gambarPupuk = R.drawable.ic_fertilizer_placeholder
            ),


            Riwayat(
                tanggal = "02-01-2024",
                namaTanaman = "Jagung",
                nitrogen = "3%",
                fosfor = "2%",
                kalium = "2.5%",
                temperatur = "28°C",
                kelembapan = "65%",
                rekomendasiPupuk = "NPK",
                gambarPupuk = R.drawable.ic_fertilizer_placeholder
            )
        )

        val riwayatAdapter = RiwayatAdapter(dummyData)
        binding.rvRiwayat.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = riwayatAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
