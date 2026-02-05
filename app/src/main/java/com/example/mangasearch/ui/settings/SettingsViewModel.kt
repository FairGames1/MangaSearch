package com.example.mangasearch.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mangasearch.data.local.SettingsManager

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val _nsfwEnabled = MutableLiveData<Boolean>()
    val nsfwEnabled: LiveData<Boolean> = _nsfwEnabled

    private val _darkModeEnabled = MutableLiveData<Boolean>()
    val darkModeEnabled: LiveData<Boolean> = _darkModeEnabled

    fun load() {
        val ctx = getApplication<Application>().applicationContext
        _nsfwEnabled.value = SettingsManager.isSafeMode(ctx)
        _darkModeEnabled.value = SettingsManager.isDarkModeEnabled(ctx)
    }

    fun setNsfw(enabled: Boolean) {
        val ctx = getApplication<Application>().applicationContext
        SettingsManager.setSafeMode(ctx, enabled)
        _nsfwEnabled.value = enabled
    }

    fun setDarkMode(enabled: Boolean) {
        val ctx = getApplication<Application>().applicationContext
        SettingsManager.setDarkModeEnabled(ctx, enabled)
        _darkModeEnabled.value = enabled
    }
}
