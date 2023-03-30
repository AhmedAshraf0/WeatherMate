package com.example.weathermate.home_screen.model

import com.example.weathermate.network.RemoteSource
import com.example.weathermate.weather_data_fetcher.WeatherAPIResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class HomeRepository(concreteRemoteSource: RemoteSource): HomeRepositoryInterface {
    
    override fun getWeatherData(): Flow<Response<WeatherAPIResponse>> {
        TODO("Not yet implemented")
    }
}