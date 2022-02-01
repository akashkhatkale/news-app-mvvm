package com.dailynews.dailynews.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dailynews.dailynews.repository.NewsRepository

class NewViewModelProviderFactory(
    private val repo : NewsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(repo) as T
    }
}