package com.agrivision.ui.chatbot

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.agrivision.data.companion.message
import com.agrivision.data.remote.retrofit.ApiConfig
import com.agrivision.databinding.ActivityChatBinding
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.filledButton.setOnClickListener {
            binding.tvChatResponse.text = ""
            askBot()
        }

    }

    private fun validateInputs(): Boolean {
        var isValid = true
        if (binding.inputTextChat.text.isNullOrEmpty()) {
            binding.inputTextChat.error = "Harus isi pertanyaan!"
            isValid = false
        } else {
            binding.inputTextChat.error = null
        }
        return isValid
    }

    private fun askBot(){
        binding.progressBar.visibility = View.VISIBLE
        if (!validateInputs()) {
            return
        }
        lifecycleScope.launch {
            try{
            val textChat = binding.inputTextChat.text.toString()
            val requiredData = message(textChat)
                val response = ApiConfig.getApiService().getChatResponse(requiredData)
                if (response.isSuccessful){
                    binding.tvChatResponse.text = response.body()?.reply?.parts?.firstOrNull()?.text
                } else {
                    Log.d("failed chat", response.message())
                    binding.tvChatResponse.text = response.message()
                    Toast.makeText(this@ChatActivity, "Server Error", Toast.LENGTH_SHORT).show()
                }
            } catch (e:Exception){
                Log.e("err","${e.message}")
                binding.tvChatResponse.text = "Maaf Agrimin sedang sibuk."
                Toast.makeText(this@ChatActivity, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}