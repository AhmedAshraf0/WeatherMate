package com.example.weathermate.network

import com.example.weathermate.weather_data_fetcher.WeatherAPIResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("onecall")
    suspend fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String = "minutely,alerts",
        @Query("appid") appid: String = "36b28f8865311668f7449a0cc8384cde",
        @Query("units") units: String = "default",
        @Query("lang") lang: String = "en"
    ): Response<WeatherAPIResponse>
}