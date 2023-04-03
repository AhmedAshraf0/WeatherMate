package com.example.weathermate.database

import android.content.Context
import com.example.weathermate.weather_data_fetcher.WeatherResponse
import kotlinx.coroutines.flow.Flow

class ConcreteLocalSource(context: Context): LocalSource {

    private val weatherDao: WeatherDao by lazy {
        WeatherDB.getInstance(context).getWeatherDao()
    }

    override fun getLocalWeatherDetails(): Flow<List<WeatherResponse>> {
        return weatherDao.getLocalWeatherDetails()
    }

    override suspend fun insertWeatherDetails(weatherResponse: WeatherResponse) {
        weatherDao.insertWeatherDetails(weatherResponse)
    }

    override suspend fun updateWeatherDetails(weatherResponse: WeatherResponse): Int {
        return weatherDao.updateWeatherDetails(weatherResponse)
    }
}