package com.agrivision.ui.oauth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.agrivision.data.local.DataStoreManager
import com.agrivision.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataStoreManager : DataStoreManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        dataStoreManager = DataStoreManager(requireActivity())
        lifecycleScope.launch {
            dataStoreManager.username.collect {username ->
                binding.fullNameText.text = username
            }
        }

        binding.editButton.setOnClickListener{
            logout()
        }


        return binding.root
    }

    private fun logout(){

        lifecycleScope.launch {

            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK

            startActivity(intent)
            activity?.finish()
            dataStoreManager.clear()
        }

    }

}