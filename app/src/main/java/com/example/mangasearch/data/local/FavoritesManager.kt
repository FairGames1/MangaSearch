package com.example.mangasearch.data.local

import android.content.Context
import com.example.mangasearch.data.model.MangaUi
import org.json.JSONArray
import org.json.JSONObject

object FavoritesManager {

    private const val PREF_NAME = "favorites_storage"
    private const val KEY_FAVORITES = "favorites_json"

    // ----- public API -----

    fun isFavorite(context: Context, manga: MangaUi): Boolean {
        val favorites = loadFavorites(context)
        return favorites.any { it.id == manga.id }
    }

    fun addFavorite(context: Context, manga: MangaUi) {
        val favorites = loadFavorites(context)
        // remove any existing with same id, then add
        val filtered = favorites.filterNot { it.id == manga.id }.toMutableList()
        filtered.add(manga)
        saveFavorites(context, filtered)
    }

    fun removeFavorite(context: Context, manga: MangaUi) {
        val favorites = loadFavorites(context)
        val filtered = favorites.filterNot { it.id == manga.id }
        saveFavorites(context, filtered)
    }

    fun toggleFavorite(context: Context, manga: MangaUi) {
        if (isFavorite(context, manga)) {
            removeFavorite(context, manga)
        } else {
            addFavorite(context, manga)
        }
    }

    fun getFavorites(context: Context): List<MangaUi> {
        return loadFavorites(context)
    }

    // ----- internal storage helpers -----

    private fun loadFavorites(context: Context): MutableList<MangaUi> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_FAVORITES, null) ?: return mutableListOf()

        val list = mutableListOf<MangaUi>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val id = obj.optString("id", "")
                val title = obj.optString("title", "")
                val author = obj.optString("author", null)
                val description = obj.optString("description", null)
                val coverUrl = obj.optString("coverUrl", null)

                val tagsArray = obj.optJSONArray("tags")
                val tags = mutableListOf<String>()
                if (tagsArray != null) {
                    for (j in 0 until tagsArray.length()) {
                        tags.add(tagsArray.optString(j))
                    }
                }

                if (id.isNotBlank()) {
                    list.add(
                        MangaUi(
                            id = id,
                            title = title,
                            author = author,
                            description = description,
                            coverUrl = coverUrl,
                            tags = tags
                        )
                    )
                }
            }
        } catch (e: Exception) {
            // if something is corrupted, just return empty list instead of crashing
            e.printStackTrace()
            return mutableListOf()
        }

        return list
    }

    private fun saveFavorites(context: Context, favorites: List<MangaUi>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val array = JSONArray()

        favorites.forEach { manga ->
            val obj = JSONObject()
            obj.put("id", manga.id)
            obj.put("title", manga.title)
            obj.put("author", manga.author)
            obj.put("description", manga.description)
            obj.put("coverUrl", manga.coverUrl)

            val tagsArray = JSONArray()
            manga.tags.forEach { tag -> tagsArray.put(tag) }
            obj.put("tags", tagsArray)

            array.put(obj)
        }

        prefs.edit()
            .putString(KEY_FAVORITES, array.toString())
            .apply()
    }
}
