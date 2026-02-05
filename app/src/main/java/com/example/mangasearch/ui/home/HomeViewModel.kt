package com.example.mangasearch.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mangasearch.data.model.MangaUi
import com.example.mangasearch.data.repository.MangaRepository
import kotlinx.coroutines.launch


class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = MangaRepository(application.applicationContext)

    private val _mangaList = MutableLiveData<List<MangaUi>>()
    val mangaList: LiveData<List<MangaUi>> = _mangaList

    fun loadPopular() {
        viewModelScope.launch {
            try {
                _mangaList.value = repo.getPopularManga()
            } catch (e: Exception) {
                _mangaList.value = emptyList()
            }
        }
    }
}
