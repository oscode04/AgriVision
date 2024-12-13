package com.agrivision.ui.artikel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agrivision.data.remote.response.ArticleResponseItem
import com.agrivision.data.remote.retrofit.ApiConfig
import com.agrivision.data.remote.retrofit.ApiService
import kotlinx.coroutines.launch
import android.util.Log
class ArtikelViewModel : ViewModel() {
    private val _articlesData = MutableLiveData<List<ArticleResponseItem>>()
    val articlesItem: LiveData<List<ArticleResponseItem>> = _articlesData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _search = MutableLiveData<List<ArticleResponseItem>>()
    val search: LiveData<List<ArticleResponseItem>> = _search

    private val _isErr = MutableLiveData<String>()
    val isErr: LiveData<String> = _isErr

    private val _isRetry = MutableLiveData<Boolean>()
    val isRetry: LiveData<Boolean> = _isRetry

    init {
        getData()
    }

    fun getData(){
        viewModelScope.launch {
            _isRetry.value = false
            _isLoading.value = true
            _isErr.value = "no error"
            try {
                val response = ApiConfig.getApiService().getArticles()
                if (response != null) {
                    _articlesData.value = response.body()
                    _search.value = response.body()

                } else {
                    _isErr.value = "Gagal Fetch Artikel"
                    _isRetry.value = true
                    Log.e("Failed getartikel", "respons null")
                }
            } catch (e: Exception) {
                _isErr.value = "Tidak ada internet"
                _isRetry.value = true
                Log.e("MainViewmodel", "invaldi")
            }
            _isLoading.value = false
        }
    }

    fun searchArticles(query: String) {
        val currentList = _articlesData.value ?: emptyList()
        _search.value = if (query.isEmpty()) {
            currentList
        } else {
            currentList.filter {
                it.jsonMember4Title.contains(query, ignoreCase = true) ||
                it.jsonMember3Author.contains(query, ignoreCase = true)
            }
        }
    }

}