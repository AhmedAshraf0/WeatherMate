package com.example.weathermate.database

import com.example.weathermate.weather_data_fetcher.FavoriteWeatherResponse
import com.example.weathermate.weather_data_fetcher.WeatherResponse

class FakeLocalDataSource(
    private var weatherResponseList: MutableList<WeatherResponse> = mutableListOf<WeatherResponse>()
) : LocalSource {

    override fun getLocalWeatherDetails(): List<WeatherResponse> {
        return weatherResponseList
    }

    override suspend fun insertWeatherDetails(weatherResponse: WeatherResponse) {
        weatherResponseList.add(weatherResponse)
    }

    override fun getLocalFavDetails(): List<FavoriteWeatherResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun insertNewFavorite(favoriteWeatherResponse: FavoriteWeatherResponse) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFavorite(favoriteWeatherResponse: FavoriteWeatherResponse) {
        TODO("Not yet implemented")
    }
}