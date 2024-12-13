package com.agrivision

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.agrivision.data.local.DataStoreManager
import com.agrivision.databinding.ActivityMainBinding
import com.agrivision.ui.oauth.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val navView: BottomNavigationView = binding.navView

            val navController = findNavController(R.id.nav_host_fragment_activity_main)

            binding.navView.setOnApplyWindowInsetsListener(null)
            binding.navView.setPadding(0, 0, 0, 0)
            navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> {
                    navView.menu.findItem(R.id.navigation_home).setIcon(R.drawable.home_filled)
                    navView.menu.findItem(R.id.navigation_artikel).setIcon(R.drawable.article_outline)
                    navView.menu.findItem(R.id.navigation_profile).setIcon(R.drawable.person_outline)
                }
                R.id.navigation_artikel -> {
                    navView.menu.findItem(R.id.navigation_home).setIcon(R.drawable.home_outline)
                    navView.menu.findItem(R.id.navigation_artikel).setIcon(R.drawable.article_filled)
                    navView.menu.findItem(R.id.navigation_profile).setIcon(R.drawable.person_outline)
                }
                R.id.navigation_profile -> {
                     navView.menu.findItem(R.id.navigation_home).setIcon(R.drawable.home_outline)
                    navView.menu.findItem(R.id.navigation_artikel).setIcon(R.drawable.article_outline)
                    navView.menu.findItem(R.id.navigation_profile).setIcon(R.drawable.person_filled)
                }
            }
        }
    }
}



