package com.example.mangasearch.data.model

data class AtHomeResponse(
    val baseUrl: String,
    val chapter: AtHomeChapter
)

data class AtHomeChapter(
    val hash: String,
    val data: List<String>,
    val dataSaver: List<String>?
)
