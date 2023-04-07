package com.example.weathermate.favorites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weathermate.home_screen.model.HomeRepositoryInterface
import com.example.weathermate.favorites.model.FavoriteRepositoryInterface

class FavoriteViewModelFactory(
    private val _repo: FavoriteRepositoryInterface,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteViewModel(_repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
