package com.example.weathermate.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.weathermate.weather_data_fetcher.FavoriteWeatherResponse
import com.example.weathermate.weather_data_fetcher.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface LocalSource {
    fun getLocalWeatherDetails() : List<WeatherResponse>
    suspend fun insertWeatherDetails(weatherResponse: WeatherResponse)
    suspend fun updateWeatherDetails(weatherResponse: WeatherResponse) : Int

    fun getLocalFavDetails() : List<FavoriteWeatherResponse>
    suspend fun insertNewFavorite(favoriteWeatherResponse: FavoriteWeatherResponse)
    suspend fun deleteFavorite(favoriteWeatherResponse: FavoriteWeatherResponse)
}