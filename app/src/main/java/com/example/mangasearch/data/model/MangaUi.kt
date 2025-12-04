package com.example.mangasearch.data.model

data class MangaUi(
    val id: String,
    val title: String,
    val author: String?,
    val description: String?,
    val coverUrl: String?,
    val tags: List<String>
)
