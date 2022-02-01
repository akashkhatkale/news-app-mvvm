package com.dailynews.dailynews.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dailynews.dailynews.R
import com.dailynews.dailynews.api.Resource
import com.dailynews.dailynews.db.ArticleDatabase
import com.dailynews.dailynews.repository.NewsRepository
import com.dailynews.dailynews.ui.MainActivity
import com.dailynews.dailynews.ui.viewmodel.NewViewModelProviderFactory
import com.dailynews.dailynews.ui.viewmodel.NewsViewModel
import com.dailynews.dailynews.util.Constants
import com.dailynews.dailynews.viewholders.NewsAdapter
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : Fragment() {

    private lateinit var viewModel : NewsViewModel
    private lateinit var newsAdapter : NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val repository = NewsRepository(ArticleDatabase(requireContext()))
        val viewModelFactory = NewViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[NewsViewModel::class.java]

        return inflater.inflate(R.layout.fragment_news, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyler()

        viewModel.breakingNews.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    progressBarVisibility(View.INVISIBLE, false)
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / 20 + 2
                        isLastPage = totalPages == viewModel.breakingPageNumber
                    }
                }

                is Resource.Error -> {
                    progressBarVisibility(View.INVISIBLE, false)
                    Log.d(Constants.NEWS_LOG, "Error loading data : ${response.message}")
                }

                is Resource.Loading -> {
                    progressBarVisibility(View.VISIBLE, true )
                }

            }
        })
    }


    private fun progressBarVisibility(v : Int, isLoading : Boolean){
        newsProgressBar.visibility = v
        this.isLoading = isLoading
    }


    var isLoading = false
    var isScrolling = false
    var isLastPage = false
    private val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 20
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                viewModel.getBreakingNews("in")
                isScrolling = false
            }

        }
    }



    private fun setUpRecyler() {
        newsAdapter = NewsAdapter()
        newsRecyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@NewsFragment.scrollListener)
        }
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().apply {
                navigate(R.id.action_newsFragment_to_selectedFragment, bundle)
            }
        }
    }

}