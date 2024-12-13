package com.agrivision.ui.artikel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.agrivision.data.remote.response.ArticleResponseItem
import com.agrivision.databinding.FragmentArtikelBinding

class ArtikelFragment : Fragment() {

    private var _binding: FragmentArtikelBinding? = null
    private val binding get() = _binding!!

    private lateinit var rvNews: RecyclerView
    private val listArticleAdapter = ListArticleAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtikelBinding.inflate(inflater, container, false)
        val artikelViewModel =
            ViewModelProvider(this).get(ArtikelViewModel::class.java)


        val root: View = binding.root

        rvNews = binding.rvNews
        rvNews.setHasFixedSize(true)
        rvNews.layoutManager = LinearLayoutManager(requireActivity())
        rvNews.adapter = listArticleAdapter

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                artikelViewModel.searchArticles(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                artikelViewModel.searchArticles(newText ?: "")
                return true
            }
        })

        binding.btnRetry.setOnClickListener {
            artikelViewModel.getData()
        }

        artikelViewModel.articlesItem.observe(viewLifecycleOwner) { articleList ->
            setArticlesData(articleList)

        }
        artikelViewModel.search.observe(viewLifecycleOwner) { searchArticle ->
            setArticlesData(searchArticle)
        }

        artikelViewModel.isLoading.observe(viewLifecycleOwner) {
            loading(it)
        }

        artikelViewModel.isRetry.observe(viewLifecycleOwner) {
            retry(it)
        }

        artikelViewModel.isErr.observe(viewLifecycleOwner) {
            if (it == "no error" || it.isNullOrEmpty()) {
                Log.d("fetch artikel", "sukses coy")
            } else {
                Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }


    private fun loading(isLoading: Boolean) {
        binding.let {
            if (isLoading != false) {
                binding.progressBar2.visibility = View.VISIBLE
            } else {
                binding.progressBar2.visibility = View.GONE
            }
        }
    }

    private fun retry(isRetry: Boolean) {
        if (isRetry != false) {
            binding.btnRetry.visibility = View.VISIBLE

        } else {
            binding.btnRetry.visibility = View.GONE
        }
    }

    private fun setArticlesData(dataEvents: List<ArticleResponseItem>) {
        listArticleAdapter.apply {
            listArticle.clear()
            listArticle.addAll(dataEvents)
            notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
