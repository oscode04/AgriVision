package com.agrivision.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agrivision.data.remote.response.ArticleResponse
import com.agrivision.data.remote.response.ArticleResponseItem
import com.agrivision.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private val _detailArticle = MutableLiveData<ArticleResponseItem>()
    val detailArticle: LiveData<ArticleResponseItem> = _detailArticle
    private val _eventArticleId = MutableLiveData<String>()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRetry = MutableLiveData<Boolean>()
    val isRetry: LiveData<Boolean> = _isRetry
    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isErr = MutableLiveData<String>()
    val isErr: LiveData<String> = _isErr


    fun setDetailId(id: String) {
        _eventArticleId.value = id
    }

    init {
        _eventArticleId.observeForever { id ->
            setArticleDetail(id)
        }
    }

    fun setArticleDetail(id: String) {
        _isLoading.value = true
        _isSuccess.value = false
        _isRetry.value = false
        viewModelScope.launch {

            try {
                val response = ApiConfig.getApiService().getArticlesDetail(id)
                if (response != null) {
                    _detailArticle.value = response.body()
                    _isRetry.value = false
                    _isSuccess.value = true
                } else {
                    _isErr.value = "Gagal Fetch Story"
                    _isRetry.value = true
                    Log.e("Failed getStories", "respons null")
                }
            } catch (e: Exception) {
                _isErr.value = "Periksa koneksi internet anda"
                _isRetry.value = true
                Log.e("MainViewmodel", "invaldi")
            } finally {
                _isLoading.value = false
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        _eventArticleId.removeObserver { id -> setArticleDetail(id) }
    }
}
