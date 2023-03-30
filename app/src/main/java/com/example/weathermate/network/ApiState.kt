package com.example.weathermate.network

import com.example.weathermate.weather_data_fetcher.WeatherAPIResponse


sealed class ApiState{
    class Success(val data : WeatherAPIResponse) : ApiState()
    class Failure(val msg : Throwable) : ApiState()
    object Loading : ApiState()
}
