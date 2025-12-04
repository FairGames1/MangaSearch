package com.example.mangasearch.data.model

data class MangaListResponse(
    val data: List<MangaData>
)

data class MangaData(
    val id: String,
    val attributes: MangaAttributes,
    val relationships: List<Relationship>
)

data class MangaAttributes(
    val title: Map<String, String>,
    val description: Map<String, String>?,
    val status: String?,
    val tags: List<Tag>?
)

data class Tag(
    val attributes: TagAttributes
)

data class TagAttributes(
    val name: Map<String, String>
)

data class Relationship(
    val id: String,
    val type: String,
    val attributes: RelationshipAttributes?
)

data class RelationshipAttributes(
    val fileName: String? = null,     // for cover_art
    val name: String? = null          // for author
)