package com.example.mangasearch.data.repository

import com.example.mangasearch.data.api.ApiClient
import com.example.mangasearch.data.api.MangaDexService
import com.example.mangasearch.data.model.*

class MangaRepository {

    private val service: MangaDexService =
        ApiClient.retrofit.create(MangaDexService::class.java)

    suspend fun getPopularManga(): List<MangaUi> {
        val response = service.getPopularManga()
        return response.data.map { it.toMangaUi() }
    }

    suspend fun searchManga(title: String): List<MangaUi> {
        val response = service.searchManga(title)
        return response.data.map { it.toMangaUi() }
    }

    suspend fun getChapters(mangaId: String): List<ChapterUi> {
        val response = service.getChapters(mangaId)
        return response.data.map {
            ChapterUi(
                id = it.id,
                number = it.attributes.chapter ?: "?",
                title = it.attributes.title,
                date = it.attributes.readableAt
            )
        }
    }

    suspend fun getPages(chapterId: String): List<PageUi> {
        val atHome = service.getAtHome(chapterId)
        val base = atHome.baseUrl
        val hash = atHome.chapter.hash

        val files = when {
            !atHome.chapter.data.isNullOrEmpty() -> atHome.chapter.data
            !atHome.chapter.dataSaver.isNullOrEmpty() -> atHome.chapter.dataSaver
            else -> emptyList()
        }

        return files.map { fileName ->
            PageUi(url = "$base/data/$hash/$fileName")
        }
    }

    private fun MangaData.toMangaUi(): MangaUi {
        val mainTitle = attributes.title["en"]
            ?: attributes.title.values.firstOrNull()
            ?: "Unknown"

        val description = attributes.description?.get("en")
            ?: attributes.description?.values?.firstOrNull()

        val authorRel = relationships.firstOrNull { it.type == "author" }
        val authorName = authorRel?.attributes?.name

        val coverRel = relationships.firstOrNull { it.type == "cover_art" }
        val coverFile = coverRel?.attributes?.fileName
        val coverUrl = coverFile?.let {
            "https://uploads.mangadex.org/covers/${id}/$it.256.jpg"
        }

        val tags = attributes.tags?.mapNotNull { it.attributes.name["en"] } ?: emptyList()

        return MangaUi(
            id = id,
            title = mainTitle,
            author = authorName,
            description = description,
            coverUrl = coverUrl,
            tags = tags
        )
    }
}
