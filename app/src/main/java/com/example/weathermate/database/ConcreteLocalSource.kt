package com.example.weathermate.database

import android.content.Context
import com.example.weathermate.weather_data_fetcher.FavoriteWeatherResponse
import com.example.weathermate.weather_data_fetcher.WeatherResponse
import kotlinx.coroutines.flow.Flow

class ConcreteLocalSource(private val weatherDao: WeatherDao): LocalSource {

    /*private val weatherDao: WeatherDao by lazy {
        WeatherDB.getInstance(context).getWeatherDao()
    }*/

    override fun getLocalWeatherDetails(): List<WeatherResponse>{
        return weatherDao.getLocalWeatherDetails()
    }

    override suspend fun insertWeatherDetails(weatherResponse: WeatherResponse) {
        weatherDao.insertWeatherDetails(weatherResponse)
    }

    /*override suspend fun updateWeatherDetails(weatherResponse: WeatherResponse): Int {
        return weatherDao.updateWeatherDetails(weatherResponse)
    }*/

    override fun getLocalFavDetails(): List<FavoriteWeatherResponse> {
        return weatherDao.getLocalFavDetails()
    }

    override suspend fun insertNewFavorite(favoriteWeatherResponse: FavoriteWeatherResponse) {
        weatherDao.insertNewFavorite(favoriteWeatherResponse)
    }

    override suspend fun deleteFavorite(favoriteWeatherResponse: FavoriteWeatherResponse) {
        weatherDao.deleteFavorite(favoriteWeatherResponse)
    }
}