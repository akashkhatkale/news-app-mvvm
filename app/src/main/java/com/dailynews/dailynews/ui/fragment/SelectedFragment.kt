package com.dailynews.dailynews.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.dailynews.dailynews.R
import com.dailynews.dailynews.ui.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_selected.*

class SelectedFragment : Fragment() {

    private val viewModel : NewsViewModel by activityViewModels()
    private val args : SelectedFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selected, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // load webview
        val article = args.article
        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }


        // bookmark button
        bookmarkButton.setOnClickListener {
            viewModel.upsert(article)
            Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }

    }


}