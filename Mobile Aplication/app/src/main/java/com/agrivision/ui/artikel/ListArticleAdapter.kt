package com.agrivision.ui.artikel

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target
import androidx.recyclerview.widget.RecyclerView
import com.agrivision.R
import com.agrivision.data.remote.response.ArticleResponse
import com.agrivision.data.remote.response.ArticleResponseItem
import com.agrivision.databinding.ItemArticleBinding
import com.agrivision.ui.detail.DetailActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.RequestListener

class ListArticleAdapter(val listArticle: ArrayList<ArticleResponseItem>) : RecyclerView.Adapter<ListArticleAdapter.ListViewHolder>() {


    class ListViewHolder(private var binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind (articles: ArticleResponseItem){
           articles.jsonMember5ImageURL.let {
                   Glide.with(binding.imgItemPhoto.context)
                       .load(it)
                       .placeholder(R.drawable.ic_clock_local)
                       .timeout(5000)
                       .error(R.drawable.ic_placeholder)
                       .listener(object : RequestListener<Drawable> {

                           override fun onResourceReady(
                               resource: Drawable,
                               model: Any,
                               target: Target<Drawable>?,
                               dataSource: DataSource,
                               isFirstResource: Boolean
                           ): Boolean {
                               binding.progressBar.visibility = View.GONE
                               return false
                           }

                           override fun onLoadFailed(
                               e: GlideException?,
                               model: Any?,
                               target: Target<Drawable>,
                               isFirstResource: Boolean
                           ): Boolean {
                               binding.progressBar.visibility = View.GONE
                               return false
                           }
                       })
                       .into(binding.imgItemPhoto)
               }
            binding.tvItemAuthor.text = articles.jsonMember3Author
            binding.tvItemTitle.text = articles.jsonMember4Title
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = listArticle.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val articles = listArticle[position]
        holder.bind(articles)

        holder.itemView.setOnClickListener {

            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra("id", articles.id)
            intentDetail.putExtra("date", articles.jsonMember2Date)
            intentDetail.putExtra("title", articles.jsonMember4Title)
            intentDetail.putExtra("author", articles.jsonMember3Author)
            intentDetail.putExtra("imageLogo", articles.jsonMember5ImageURL)
            intentDetail.putExtra("desc", articles.jsonMember6Content)
            holder.itemView.context.startActivity(intentDetail)

        }
    }
}
