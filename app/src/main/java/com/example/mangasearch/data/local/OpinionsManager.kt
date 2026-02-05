package com.example.mangasearch.data.local

import android.content.Context
import com.example.mangasearch.data.model.Opinion
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object OpinionsManager {
    private const val PREFS = "opinions_prefs"
    private const val KEY = "opinions_json"
    private val gson = Gson()

    fun getOpinions(context: Context, mangaId: String): List<Opinion> {
        return readAll(context)
            .filter { it.mangaId == mangaId }
            .sortedByDescending { it.createdAt }
    }

    fun addOpinion(context: Context, opinion: Opinion) {
        val all = readAll(context).toMutableList()
        all.add(opinion)
        writeAll(context, all)
    }

    private fun readAll(context: Context): List<Opinion> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY, null) ?: return emptyList()
        val type = object : TypeToken<List<Opinion>>() {}.type
        return runCatching { gson.fromJson<List<Opinion>>(json, type) }.getOrDefault(emptyList())
    }

    private fun writeAll(context: Context, opinions: List<Opinion>) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY, gson.toJson(opinions)).apply()
    }
}