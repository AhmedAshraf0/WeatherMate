package com.example.weathermate.database

import com.example.weathermate.weather_data_fetcher.FavoriteWeatherResponse
import com.example.weathermate.weather_data_fetcher.WeatherResponse


sealed class DbState {
    class Success(
        val data: WeatherResponse? = null,
        val favoriteWeatherResponse: List<FavoriteWeatherResponse>? = null
    ) : DbState()

    class Failure(val msg: Throwable) : DbState()
    object Loading : DbState()
}
