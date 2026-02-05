package com.example.mangasearch.ui.chapters

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mangasearch.data.model.ChapterUi
import com.example.mangasearch.data.repository.MangaRepository
import kotlinx.coroutines.launch

class ChapterListViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = MangaRepository(application.applicationContext)

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
