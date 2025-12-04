package com.example.mangasearch.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangasearch.data.model.MangaUi
import com.example.mangasearch.data.repository.MangaRepository
import kotlinx.coroutines.launch

class SearchResultsViewModel : ViewModel() {

    private val repo = MangaRepository()

    private val _mangaList = MutableLiveData<List<MangaUi>>()
    val mangaList: LiveData<List<MangaUi>> = _mangaList

    fun search(query: String) {
        viewModelScope.launch {
            try {
                _mangaList.value = repo.searchManga(query)
            } catch (_: Exception) {
            }
        }
    }
}
