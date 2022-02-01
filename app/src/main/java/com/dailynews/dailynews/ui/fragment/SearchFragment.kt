package com.dailynews.dailynews.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dailynews.dailynews.R
import com.dailynews.dailynews.api.Resource
import com.dailynews.dailynews.ui.MainActivity
import com.dailynews.dailynews.ui.viewmodel.NewsViewModel
import com.dailynews.dailynews.util.Constants
import com.dailynews.dailynews.viewholders.NewsAdapter
import kotlinx.android.synthetic.main.fragment_news.*
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {

    val viewModel : NewsViewModel by activityViewModels()
    private lateinit var newsAdapter : NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyler()

        // search
        searchButton.setOnClickListener {
            val search = searchEditText.text.toString()
            if (search.isNotEmpty()){
                viewModel.getSearchNews(search)
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    progressBarVisibility(View.INVISIBLE, View.INVISIBLE)
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }

                is Resource.Error -> {
                    progressBarVisibility(View.INVISIBLE, View.VISIBLE)
                }

                is Resource.Loading -> {
                    progressBarVisibility(View.VISIBLE, View.INVISIBLE)

                }

            }
        })
    }

    private fun progressBarVisibility(v : Int, status : Int){
        searchProgress.visibility = v
        searchStatus.visibility = status
    }

    private fun setUpRecyler() {
        newsAdapter = NewsAdapter()
        searchRecyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}