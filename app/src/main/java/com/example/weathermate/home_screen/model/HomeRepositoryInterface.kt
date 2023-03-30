package com.example.weathermate.home_screen.model

import com.example.weathermate.weather_data_fetcher.WeatherAPIResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface HomeRepositoryInterface {
    fun getWeatherData() : Flow<Response<WeatherAPIResponse>>
}