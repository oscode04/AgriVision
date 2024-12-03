package com.agrivision.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.agrivision.R
import com.agrivision.data.response.ArticleResponse
import com.agrivision.data.response.ArticleResponseItem
import com.agrivision.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        val id = intent.getStringExtra("id")
        if (id != null) {
            detailViewModel.setDetailId(id)
            Log.d("ingpo id", id)
        }

        detailViewModel.detailArticle.observe(this) { detailArticle ->
            setAriticleDetail(detailArticle)
        }
        detailViewModel.isLoading.observe(this) { isLoading ->
            loading(isLoading)
        }
//        detailViewModel.isSuccess.observe(this) {
//            showItem(it)
//        }
//        detailViewModel.isErr.observe(this) {
//            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
//        }

        setContentView(binding.root)



    }

    private fun setAriticleDetail(article : ArticleResponseItem) {
        Glide.with(this)
            .load(article.jsonMember5ImageURL)
            .into(binding.imgHeader)
        binding.tvTitle.text = article.jsonMember4Title
        binding.tvDate.text=article.jsonMember2Date
        binding.tvAuthor.text = article.jsonMember3Author

//        binding.tvBeginTime.text = events.beginTime
//        binding.tvRegistrant.text = events.registrants.toString()
//        binding.tvQuota.text = events.quota.toString()
        binding.tvDescription.text= HtmlCompat.fromHtml(
            article.jsonMember6Content,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )


    }

    fun loading(isLoading: Boolean) {
        if (isLoading != false) {
            binding.progressBar.visibility = View.VISIBLE

        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}