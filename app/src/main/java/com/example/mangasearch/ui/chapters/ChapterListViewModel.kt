package com.example.mangasearch.ui.chapters

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangasearch.data.model.ChapterUi
import com.example.mangasearch.data.repository.MangaRepository
import kotlinx.coroutines.launch

class ChapterListViewModel : ViewModel() {

    private val repo = MangaRepository()

    private val _chapterList = MutableLiveData<List<ChapterUi>>()
    val chapterList: LiveData<List<ChapterUi>> = _chapterList

    fun loadChapters(mangaId: String) {
        viewModelScope.launch {
            try {
                val chapters = repo.getChapters(mangaId)
                _chapterList.value = chapters
            } catch (e: Exception) {
                Log.e("ChapterListViewModel", "Error loading chapters", e)
                _chapterList.value = emptyList()
            }
        }
    }
}
