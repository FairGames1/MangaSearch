package com.example.mangasearch.data.model

data class ChapterListResponse(
    val data: List<ChapterData>
)

data class ChapterData(
    val id: String,
    val attributes: ChapterAttributes
)

data class ChapterAttributes(
    val chapter: String?,
    val title: String?,
    val readableAt: String?
)
