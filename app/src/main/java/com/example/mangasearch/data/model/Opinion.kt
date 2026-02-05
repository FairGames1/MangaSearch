package com.example.mangasearch.data.model

data class Opinion(
    val mangaId: String,
    val rating: Int,         // 1..5
    val comment: String,
    val createdAt: Long = System.currentTimeMillis()
)
