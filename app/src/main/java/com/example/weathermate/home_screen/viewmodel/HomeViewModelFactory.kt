package com.example.weathermate.home_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weathermate.home_screen.model.HomeRepositoryInterface

class HomeViewModelFactory(
    private val _repo: HomeRepositoryInterface,
    private val latitude: Double,
    private val longitude: Double,
    private val units: String,
    private val lang: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(_repo, latitude, longitude, units, lang) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
