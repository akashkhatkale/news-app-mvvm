package com.dailynews.dailynews.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailynews.dailynews.api.Resource
import com.dailynews.dailynews.modals.Article
import com.dailynews.dailynews.modals.NewsResponse
import com.dailynews.dailynews.repository.NewsRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val repository : NewsRepository
) : ViewModel() {

    val breakingNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingPageNumber = 1
    var breakingNewsResponse : NewsResponse? = null

    val searchNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchPageNumber = 1
    var searchNewsResponse : NewsResponse? = null


    init {
        getBreakingNews("in")
    }


    // search breaking news
    fun getBreakingNews(countryCode : String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = repository.getBreakingNews(countryCode, breakingPageNumber)
        breakingNews.postValue(handleResponseResult(response))
    }

    // get searched news
    fun getSearchNews(search : String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = repository.getSearchNews(search, searchPageNumber)
        searchNews.postValue(handleSearchResponseResult(response))
    }


    // room db methods
    fun upsert(article : Article) = viewModelScope.launch {
        repository.upsert(article)
    }

    fun getSavedNews() = repository.getSavedArticles()

    fun deleteArticle (article: Article) = viewModelScope.launch {
        repository.delete(article)
    }




    // helpers
    private fun handleResponseResult(response : Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful){
            response.body()?.let{res->
                breakingPageNumber++
                if(breakingNewsResponse == null){
                    breakingNewsResponse = res
                }else{
                    breakingNewsResponse!!.articles.addAll(res.articles)
                }
                return Resource.Success(breakingNewsResponse ?: res)
            }
        }
        return Resource.Error(response.message())
    }
    private fun handleSearchResponseResult(response : Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful){
            response.body()?.let{res->
               
                return Resource.Success( res)
            }
        }
        return Resource.Error(response.message())
    }


}
