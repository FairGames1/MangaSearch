package com.example.mangasearch.data.local

import android.content.Context

object SettingsManager {
    private const val PREFS = "app_settings"
    private const val KEY_SAFE_MODE = "nsfw_filter_enabled"
    private const val KEY_DARK_MODE = "dark_mode_enabled"

    fun isSafeMode(context: Context): Boolean =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getBoolean(KEY_SAFE_MODE, true)

    fun setSafeMode(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_SAFE_MODE, enabled)
            .apply()
    }

    fun isDarkModeEnabled(context: Context): Boolean {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getBoolean(KEY_DARK_MODE, true)
    }

    fun setDarkModeEnabled(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_DARK_MODE, enabled)
            .apply()
    }
}
