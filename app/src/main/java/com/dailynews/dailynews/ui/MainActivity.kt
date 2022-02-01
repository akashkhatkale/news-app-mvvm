package com.dailynews.dailynews.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.dailynews.dailynews.R
import com.dailynews.dailynews.db.ArticleDatabase
import com.dailynews.dailynews.repository.NewsRepository
import com.dailynews.dailynews.ui.viewmodel.NewViewModelProviderFactory
import com.dailynews.dailynews.ui.viewmodel.NewsViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = Navigation.findNavController(this, R.id.newsNavHost)

        // init
        val repository = NewsRepository(ArticleDatabase(this))
        val viewModelFactory = NewViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[NewsViewModel::class.java]


        // bottom nav with navigation component
        mainBottomNavigation.setupWithNavController(navController)
    }
}