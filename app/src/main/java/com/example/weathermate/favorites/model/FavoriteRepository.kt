package com.example.weathermate.favorites.model

import com.example.weathermate.database.LocalSource
import com.example.weathermate.home_screen.model.HomeRepository
import com.example.weathermate.network.RemoteSource
import com.example.weathermate.weather_data_fetcher.FavoriteWeatherResponse
import com.example.weathermate.weather_data_fetcher.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class FavoriteRepository(
    private val concreteRemoteSource: RemoteSource,
    private val concreteLocalSource: LocalSource
) : FavoriteRepositoryInterface {

    companion object {
        private var INSTANCE: FavoriteRepository? = null
        fun getInstance(
            concreteRemoteSource: RemoteSource,
            concreteLocalSource: LocalSource
        ): FavoriteRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = FavoriteRepository(concreteRemoteSource, concreteLocalSource)
                INSTANCE = instance
                instance
            }
        }
    }

    override fun getWeatherData(
        latitude: Double,
        longitude: Double,
        units: String,
        lang: String,
        exclude: String
    ): Flow<Response<WeatherResponse>> {
        return flow {
            emit(
                concreteRemoteSource.getWeatherData(
                    latitude,
                    longitude,
                    units,
                    lang,
                    exclude
                )
            )
        }
    }

    override fun getLocalFavDetails(): Flow<List<FavoriteWeatherResponse>> {
        return flow {
            emit(concreteLocalSource.getLocalFavDetails())
        }
    }

    override suspend fun insertNewFavorite(favoriteWeatherResponse: FavoriteWeatherResponse) {
        concreteLocalSource.insertNewFavorite(favoriteWeatherResponse)
    }

    override suspend fun deleteFavorite(favoriteWeatherResponse: FavoriteWeatherResponse) {
        concreteLocalSource.deleteFavorite(favoriteWeatherResponse)
    }
}