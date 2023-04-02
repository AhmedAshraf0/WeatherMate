package com.example.weathermate.home_screen.model

import com.example.weathermate.weather_data_fetcher.WeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface HomeRepositoryInterface {
    fun getWeatherData(
        latitude: Double,
        longitude: Double,
        units: String,
        lang: String
    ) : Flow<Response<WeatherResponse>>
}