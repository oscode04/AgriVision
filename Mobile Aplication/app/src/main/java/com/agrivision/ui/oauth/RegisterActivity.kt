package com.agrivision.ui.oauth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.agrivision.data.companion.RegisterRequest
import com.agrivision.data.remote.retrofit.ApiConfig
import com.agrivision.databinding.ActivityRegisterBinding
import com.google.gson.JsonParser
import kotlinx.coroutines.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val linkToRegister = binding.linkToLogin
        linkToRegister.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            val nickname = binding.etNickname.text.toString()

            if (email.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty() && nickname.isNotEmpty()) {
                registerUser(email, username, password, nickname)
            } else {
                Toast.makeText(this, "Harap isi semua field", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(email: String, username: String, password: String, nickname: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnRegister.visibility = View.GONE
        binding.logoImageView.visibility = View.GONE
        binding.linkToLogin.visibility = View.GONE
        binding.emailInputLayout.visibility = View.GONE
        binding.nicknameInputLayout.visibility = View.GONE
        binding.usernameInputLayout.visibility = View.GONE
        binding.passwordInputLayout.visibility = View.GONE
        lifecycleScope.launch {
            try {
                val apiService = ApiConfig.getApiService()
                val registerRequest = RegisterRequest(email, username, password, nickname)

                val response = apiService.registerUser(registerRequest)

                if (response.isSuccessful) {
                    val intent = Intent(this@RegisterActivity, VerifyEmailActivity::class.java)
                    intent.putExtra("EXTRA_USERNAME",username)
                    startActivity(intent)
                    finish()
                } else {
                    val errorBody = response.errorBody ()?.string()
                    errorBody?.let {
                        try {
                            val jsonObject =  JsonParser().parse(it).asJsonObject
                            val errorMessage = jsonObject.get("error").asString
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registrasi gagal: $errorMessage",
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Terjadi kesalahan: Registrasi gagal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@RegisterActivity, "Cek koneksi internet anda", Toast.LENGTH_SHORT).show()
                Log.e("fail register","Terjadi kesalahan: ${e.message}")
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.btnRegister.visibility = View.VISIBLE
                binding.logoImageView.visibility = View.VISIBLE
                binding.linkToLogin.visibility = View.VISIBLE
                binding.emailInputLayout.visibility = View.VISIBLE
                binding.nicknameInputLayout.visibility = View.VISIBLE
                binding.usernameInputLayout.visibility = View.VISIBLE
                binding.passwordInputLayout.visibility = View.VISIBLE
            }
        }
    }
}
