package com.dailynews.dailynews.modals

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @ColumnInfo(name = "author", defaultValue = "")
    val author: String,
    @ColumnInfo(name = "content", defaultValue = "")
    val content: String= "",
    @ColumnInfo(name = "description", defaultValue = "")
    val description: String= "",
    @ColumnInfo(name = "publishedAt", defaultValue = "")
    val publishedAt: String= "",
    val source: Source,
    @ColumnInfo(name = "title", defaultValue = "")
    val title: String= "",
    @ColumnInfo(name = "url", defaultValue = "")
    val url: String= "",
    @ColumnInfo(name = "urlToImage", defaultValue = "")
    val urlToImage: String = ""
) : Serializable
