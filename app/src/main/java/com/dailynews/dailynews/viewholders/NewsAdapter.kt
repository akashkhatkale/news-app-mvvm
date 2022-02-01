package com.dailynews.dailynews.viewholders

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dailynews.dailynews.R
import com.dailynews.dailynews.modals.Article
import com.dailynews.dailynews.util.Constants.Companion.NEWS_LOG
import kotlinx.android.synthetic.main.layout_news.view.*

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {


    inner class ArticleViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_news, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        Log.d(NEWS_LOG, "Article : ${article}")
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(newsImage)
            newsHeadline.text = article.title
            newsDesc.text = if (article.description == null) "" else article.description
            newsAuthor.text = if (article.author == null) "" else "- " + article.author
            setOnClickListener {
                onItemClickListener?.let{
                    it(article)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    private var onItemClickListener : ((Article) -> Unit)? = null
    fun setOnItemClickListener(listener : (Article) -> Unit){
        onItemClickListener = listener
    }

}