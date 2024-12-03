package com.agrivision.ui.artikel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agrivision.data.response.ArticleResponseItem
import com.agrivision.data.retrofit.ApiConfig
import com.agrivision.data.retrofit.ApiService
import kotlinx.coroutines.launch
import android.util.Log
class ArtikelViewModel : ViewModel() {
    private val _articlesData = MutableLiveData<List<ArticleResponseItem>>()
    val articlesItem: LiveData<List<ArticleResponseItem>> = _articlesData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isErr = MutableLiveData<String>()
    val isErr: LiveData<String> = _isErr

    init {
        getData()
    }

    fun getData(){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService().getArticles()
                if (response != null) {
                    _articlesData.value = response.body()
                } else {
                    _isErr.value = "Gagal Fetch Story"
                    Log.e("Failed getStories", "respons null")
                }
            } catch (e: Exception) {
                _isErr.value = "Tidak ada internet"
                Log.e("MainViewmodel", "invaldi")
            }
            _isLoading.value = false
        }
    }

}