package com.example.weathermate.network

import com.example.weathermate.weather_data_fetcher.WeatherResponse
import retrofit2.Response

class FakeRemoteDataSource : RemoteSource {
    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double,
        units: String,
        lang: String,
        exclude: String
    ): Response<WeatherResponse> {
        return Response.success(
            WeatherResponse(
                cityLatitude = latitude,
                cityLongitude = longitude,
            )
        )
    }
}