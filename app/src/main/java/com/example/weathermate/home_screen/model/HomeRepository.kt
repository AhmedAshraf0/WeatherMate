package com.example.weathermate.home_screen.model

import com.example.weathermate.network.RemoteSource
import com.example.weathermate.weather_data_fetcher.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class HomeRepository(private val concreteRemoteSource: RemoteSource) : HomeRepositoryInterface {
    companion object {
        private var INSTANCE: HomeRepository? = null
        fun getInstance(concreteRemoteSource: RemoteSource): HomeRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = HomeRepository(concreteRemoteSource)
                INSTANCE = instance
                instance
            }
        }
    }

    override fun getWeatherData(
        latitude: Double,
        longitude: Double,
        units: String,
        lang: String
    ): Flow<Response<WeatherResponse>> {
        return flow {
            emit(
                concreteRemoteSource.getWeatherData(
                    latitude = latitude,
                    longitude = longitude,
                    units = units,
                    lang = lang
                )
            )
        }
    }
}