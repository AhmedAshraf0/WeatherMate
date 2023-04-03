package com.example.weathermate.database

import com.example.weathermate.weather_data_fetcher.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface LocalSource {
    fun getLocalWeatherDetails() : Flow<List<WeatherResponse>>
    suspend fun insertWeatherDetails(weatherResponse: WeatherResponse)
    suspend fun updateWeatherDetails(weatherResponse: WeatherResponse) : Int
}