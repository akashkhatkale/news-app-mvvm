package com.dailynews.dailynews.repository

import com.dailynews.dailynews.api.RetrofitInstance
import com.dailynews.dailynews.db.ArticleDatabase
import com.dailynews.dailynews.modals.Article

class NewsRepository(
    val articleDatabase: ArticleDatabase
) {

    suspend fun getBreakingNews(countryCode : String, pageNumber : Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun getSearchNews(search : String, pageNumber: Int) =
            RetrofitInstance.api.searchNews(search, pageNumber)

    suspend fun upsert(article : Article) = articleDatabase.getArticleDao().upsert(article)

    fun getSavedArticles() = articleDatabase.getArticleDao().getAllArticles()

    suspend fun delete(article : Article) = articleDatabase.getArticleDao().deleteArticle(article)


}