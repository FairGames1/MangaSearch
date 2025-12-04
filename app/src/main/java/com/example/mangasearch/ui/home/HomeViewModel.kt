package com.example.mangasearch.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangasearch.data.model.MangaUi
import com.example.mangasearch.data.repository.MangaRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repo = MangaRepository()

    private val _mangaList = MutableLiveData<List<MangaUi>>()
    val mangaList: LiveData<List<MangaUi>> = _mangaList

    fun loadPopular() {
        viewModelScope.launch {
            try {
                _mangaList.value = repo.getPopularManga()
            } catch (e: Exception) {
            }
        }
    }
}
