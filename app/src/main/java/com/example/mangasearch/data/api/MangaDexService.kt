package com.example.mangasearch.data.api

import com.example.mangasearch.data.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MangaDexService {

    @GET("manga")
    suspend fun searchManga(
        @Query("title") title: String,
        @Query("limit") limit: Int = 20,
        @Query("contentRating[]") contentRating: List<String> = listOf("safe", "suggestive"),
        @Query("includes[]") includes: List<String> = listOf("cover_art", "author")
    ): MangaListResponse

    @GET("manga")
    suspend fun getPopularManga(
        @Query("limit") limit: Int = 20,
        @QueryMap order: Map<String, String> = mapOf("order[followedCount]" to "desc"),
        @Query("includes[]") includes: List<String> = listOf("cover_art", "author"),
        @Query("contentRating[]") contentRating: List<String> = listOf("safe", "suggestive")
    ): MangaListResponse

    @GET("chapter")
    suspend fun getChapters(
        @Query("manga") mangaId: String,
        @Query("translatedLanguage[]") language: String = "en",
        @Query("order[chapter]") orderChapter: String = "asc",
        @Query("limit") limit: Int = 50,
        @Query("contentRating[]") contentRating: List<String> = listOf("safe", "suggestive")
    ): ChapterListResponse

    @GET("at-home/server/{chapterId}")
    suspend fun getAtHome(
        @Path("chapterId") chapterId: String
    ): AtHomeResponse
}
