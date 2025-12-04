package com.example.mangasearch.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mangasearch.data.model.MangaUi

class FavoritesViewModel : ViewModel() {

    private val _favorites = MutableLiveData<List<MangaUi>>()
    val favorites: LiveData<List<MangaUi>> = _favorites

    fun setFavorites(list: List<MangaUi>) {
        _favorites.value = list
    }
}
