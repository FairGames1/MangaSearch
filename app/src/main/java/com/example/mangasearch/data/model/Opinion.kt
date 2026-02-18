package com.example.mangasearch.data.model

data class Opinion(
    val mangaId: String,
    val rating: Int,
    val comment: String,
    val createdAt: Long = System.currentTimeMillis()
)
