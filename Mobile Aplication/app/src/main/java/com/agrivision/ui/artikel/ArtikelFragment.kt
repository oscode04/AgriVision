package com.agrivision.ui.artikel

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agrivision.R
import com.agrivision.data.News
import com.agrivision.data.response.ArticleResponseItem
import com.agrivision.databinding.FragmentArtikelBinding
import com.agrivision.ui.detail.DetailActivity

class ArtikelFragment : Fragment() {

    private var _binding: FragmentArtikelBinding? = null
    private val binding get() = _binding!!

    private lateinit var rvNews: RecyclerView
    private val listArticleAdapter = ListArticleAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val artikelViewModel =
            ViewModelProvider(this).get(ArtikelViewModel::class.java)

        _binding = FragmentArtikelBinding.inflate(inflater, container, false)
        val root: View = binding.root

        rvNews = binding.rvNews
        rvNews.setHasFixedSize(true)
        rvNews.layoutManager = LinearLayoutManager(requireActivity())
        rvNews.adapter = listArticleAdapter
        // Show the list of articles
//        showRecyclerList()

        artikelViewModel.articlesItem.observe(requireActivity()){articleList ->
            setArticlesData(articleList)

        }

        artikelViewModel.isLoading.observe(requireActivity()) {
            loading(it)
        }

        return root
    }

//    private fun getListArticle(): ArrayList<ArticleResponseItem> {
//
//    }

    fun loading(isLoading: Boolean) {
        if (isLoading != false) {
            binding.progressBar2.visibility = View.VISIBLE
        } else {
            binding.progressBar2.visibility = View.GONE
        }
    }
    private fun setArticlesData(dataEvents: List<ArticleResponseItem>) {
        listArticleAdapter.apply {
            listArticle.clear()
            listArticle.addAll(dataEvents)
            notifyDataSetChanged()
        }
    }
//    fun showRecyclerList() {
//        val list = getListArticle()  // Get the list of heroes
//        rvNews.layoutManager = LinearLayoutManager(context)
//        val listArticleAdapter = ListArticleAdapter(list)
//        rvNews.adapter = listArticleAdapter
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
