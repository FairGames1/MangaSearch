package com.example.mangasearch.data.local

import android.content.Context

object ReadingProgressManager {

    private const val PREF_NAME = "reading_progress"

    fun savePage(context: Context, chapterId: String, pageIndex: Int) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putInt("page_$chapterId", pageIndex)
            .apply()
    }

    fun getSavedPage(context: Context, chapterId: String): Int {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt("page_$chapterId", 0)
    }
}
