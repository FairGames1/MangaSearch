package com.example.mangasearch.ui.reader

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mangasearch.data.model.PageUi
import com.example.mangasearch.data.repository.MangaRepository
import kotlinx.coroutines.launch

class ReaderViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = MangaRepository(application.applicationContext)

    private val _pages = MutableLiveData<List<PageUi>?>()
    val pages: LiveData<List<PageUi>?> = _pages

    fun loadPages(chapterId: String) {
        Log.d("ReaderViewModel", "loadPages($chapterId)")
        viewModelScope.launch {
            try {
                val result = repo.getPages(chapterId)
                Log.d("ReaderViewModel", "Loaded ${result.size} pages")
                _pages.value = result
            } catch (e: Exception) {
                Log.e("ReaderViewModel", "Error loading pages", e)
                _pages.value = emptyList()
            }
        }
    }
}
