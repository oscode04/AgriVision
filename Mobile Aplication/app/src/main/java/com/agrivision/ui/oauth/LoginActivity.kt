package com.agrivision.ui.oauth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.agrivision.MainActivity
import com.agrivision.data.companion.LoginRequest
import com.agrivision.data.local.DataStoreManager
import com.agrivision.data.remote.response.LoginResponse
import com.agrivision.data.remote.retrofit.ApiConfig
import com.agrivision.databinding.ActivityLoginBinding
import com.google.gson.JsonParser
import kotlinx.coroutines.launch
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val emailOrUsername = binding.etEmailOrUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (emailOrUsername.isNotEmpty() && password.isNotEmpty()) {
                loginUser(emailOrUsername, password)
            } else {
                Toast.makeText(this, "Harap isi semua field", Toast.LENGTH_SHORT).show()
            }
        }
        binding.linkToRegister.setOnClickListener{onRegisterLinkClick()}
    }

    fun onRegisterLinkClick() {
        val intent = Intent(
            this@LoginActivity,
            RegisterActivity::class.java
        )
        startActivity(intent)
        finish()
    }

    private fun loginUser(emailOrUsername: String, password: String) {
        val dataStoreManager = DataStoreManager(this)
        binding.progressBar.visibility = View.VISIBLE
        binding.btnLogin.visibility = View.GONE
        binding.logoImageView.visibility = View.GONE
        binding.linkToRegister.visibility = View.GONE
        binding.emailInputLayout.visibility = View.GONE
        binding.passwordInputLayout.visibility = View.GONE
        lifecycleScope.launch {
            try {
                val response = ApiConfig.getApiService().loginUser(
                    LoginRequest(emailOrUsername, password)
                )

                if (response.isSuccessful) {
                    val nickname = response.body()?.nickname
                    if (nickname != null) {
                        dataStoreManager.saveUsername(nickname)
                    }
                    Toast.makeText(this@LoginActivity, "Login berhasil!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity,MainActivity::class.java)
                    intent.putExtra("EXTRA_USERNAME", nickname)
                    startActivity(intent)
                    finish()
                } else {
                    val error = response.errorBody()?.string()
                    error?.let {
                        try {
                            val jsonObject =  JsonParser().parse(it).asJsonObject
                            val errorMessage = jsonObject.get("error").asString
                            Toast.makeText(this@LoginActivity, "Login gagal: $errorMessage", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@LoginActivity,
                                "Terjadi kesalahan: Login gagal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.btnLogin.visibility = View.VISIBLE
                binding.logoImageView.visibility = View.VISIBLE
                binding.linkToRegister.visibility = View.VISIBLE
                binding.emailInputLayout.visibility = View.VISIBLE
                binding.passwordInputLayout.visibility = View.VISIBLE
            }
        }
    }
}
