package com.example.weathermate.home_screen.model

import com.example.weathermate.database.LocalSource
import com.example.weathermate.network.RemoteSource
import com.example.weathermate.weather_data_fetcher.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class HomeRepository(
    private val concreteRemoteSource: RemoteSource,
    private val concreteLocalSource: LocalSource
    ) : HomeRepositoryInterface {
    companion object {
        private var INSTANCE: HomeRepository? = null
        fun getInstance(concreteRemoteSource: RemoteSource,concreteLocalSource: LocalSource): HomeRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = HomeRepository(concreteRemoteSource,concreteLocalSource)
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

    override fun getLocalWeatherDetails(): Flow<List<WeatherResponse>> {
        return flow{
            emit(concreteLocalSource.getLocalWeatherDetails())
        }
    }

    override suspend fun insertWeatherDetails(weatherResponse: WeatherResponse) {
        concreteLocalSource.insertWeatherDetails(weatherResponse)
    }

    /*override suspend fun updateWeatherDetails(weatherResponse: WeatherResponse): Int {
        return concreteLocalSource.updateWeatherDetails(weatherResponse)
    }*/
}