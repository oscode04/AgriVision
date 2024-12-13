package com.agrivision.ui.oauth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.agrivision.data.companion.VerifyRequest
import com.agrivision.data.remote.retrofit.ApiConfig
import com.agrivision.databinding.ActivityVerifyEmailBinding
import com.google.gson.JsonParser

import kotlinx.coroutines.launch

class VerifyEmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerifyEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnVerify.setOnClickListener {
            val verificationCode = binding.etVerificationCode.text.toString().uppercase()

            if (verificationCode.isNotEmpty()) {
                verifyEmailCode(verificationCode)
            } else {
                Toast.makeText(this, "Harap masukkan kode verifikasi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifyEmailCode(verificationCode: String) {
        lifecycleScope.launch {
            try {
                val username = intent.getStringExtra("EXTRA_USERNAME")

                val verifyRequest = VerifyRequest(username, verificationCode)

                val response = ApiConfig.getApiService().verifyEmail(verifyRequest)

                if (response.isSuccessful) {
                    Toast.makeText(this@VerifyEmailActivity, "Verifikasi berhasil!", Toast.LENGTH_SHORT).show()
                    navigateToLogin()
                } else {
                    val errorBody = response.errorBody ()?.string()
                    errorBody?.let {
                        try {
                            val jsonObject =  JsonParser().parse(it).asJsonObject
                            val errorMessage = jsonObject.get("error").asString
                            Toast.makeText(
                                this@VerifyEmailActivity,
                                "Verifikasi gagal: $errorMessage",
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@VerifyEmailActivity,
                                "Terjadi kesalahan: Registrasi gagal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }
            } catch (e: Exception) {
                Toast.makeText(this@VerifyEmailActivity, "Cek koneksi internet", Toast.LENGTH_SHORT).show()
                Log.e( "gagal verf","Terjadi kesalahan: ${e.message}")
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
