package com.example.weathermate.favorites.model

import com.example.weathermate.weather_data_fetcher.FavoriteWeatherResponse
import com.example.weathermate.weather_data_fetcher.WeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface FavoriteRepositoryInterface {
    fun getWeatherData(
        latitude: Double,
        longitude: Double,
        units: String,
        lang: String,
        exclude: String
    ) : Flow<Response<WeatherResponse>>

    fun getLocalFavDetails(): Flow<List<FavoriteWeatherResponse>>

    suspend fun insertNewFavorite(favoriteWeatherResponse: FavoriteWeatherResponse)

    suspend fun deleteFavorite(favoriteWeatherResponse: FavoriteWeatherResponse)
}