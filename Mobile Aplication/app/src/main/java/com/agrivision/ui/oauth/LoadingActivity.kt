package com.agrivision.ui.oauth

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.agrivision.MainActivity
import com.agrivision.R
import com.agrivision.data.local.DataStoreManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loading)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }

    override fun onStart() {
        super.onStart()
        val dataStoreManager = DataStoreManager(this)
        lifecycleScope.launch {
            dataStoreManager.username.collect { username ->
                if (username.isNullOrEmpty()) {
                    val intent = Intent(this@LoadingActivity, LoginActivity::class.java)
                    delay(1500L)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@LoadingActivity, MainActivity::class.java)
                    delay(1000L)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}