package com.example.weathermate.network

import com.example.weathermate.weather_data_fetcher.WeatherAPIResponse
import retrofit2.Response

interface RemoteSource {
    suspend fun getWeatherData(
        latitude: Double,
        longitude: Double,
        units: String = "default",
        lang: String = "en"
        ): Response<WeatherAPIResponse>
}