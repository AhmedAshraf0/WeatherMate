package com.example.weathermate.network

import com.example.weathermate.weather_data_fetcher.WeatherResponse


sealed class ApiState{
    class Success(val data : WeatherResponse) : ApiState()
    class Failure(val msg : Throwable) : ApiState()
    object Loading : ApiState()
}
