package com.example.weathermate.network


import com.example.weathermate.weather_data_fetcher.WeatherAPIResponse
import retrofit2.Response

class ConcreteRemoteSource : RemoteSource{
    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double,
        units: String,
        lang: String
    ): Response<WeatherAPIResponse> {
        return API.retrofitService.getWeatherData(
            lat = latitude,
            lon = longitude,
            units = units,
            lang = lang
        )
    }


}