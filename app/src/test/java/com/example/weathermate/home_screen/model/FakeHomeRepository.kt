package com.example.weathermate.home_screen.model

import com.example.weathermate.database.LocalSource
import com.example.weathermate.network.RemoteSource
import com.example.weathermate.weather_data_fetcher.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class FakeHomeRepository(
    private val localSource: LocalSource,
    private val remoteSource: RemoteSource
)  : HomeRepositoryInterface{


    override fun getWeatherData(
        latitude: Double,
        longitude: Double,
        units: String,
        lang: String
    ): Flow<Response<WeatherResponse>> {
        return flow{
            emit(remoteSource.getWeatherData(latitude,longitude))
        }
    }

    override fun getLocalWeatherDetails(): Flow<List<WeatherResponse>> {
        return flow {
            emit(localSource.getLocalWeatherDetails())
        }
    }

    override suspend fun insertWeatherDetails(weatherResponse: WeatherResponse) {
        localSource.insertWeatherDetails(weatherResponse)
    }
}