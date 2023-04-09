package com.example.weathermate.favorites.view

import androidx.recyclerview.widget.DiffUtil
import com.example.weathermate.weather_data_fetcher.DailyForecast
import com.example.weathermate.weather_data_fetcher.FavoriteWeatherResponse
import com.example.weathermate.weather_data_fetcher.HourlyForecast

class DiffUtilFavorites : DiffUtil.ItemCallback<FavoriteWeatherResponse>() {
    override fun areItemsTheSame(oldItem: FavoriteWeatherResponse, newItem: FavoriteWeatherResponse): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: FavoriteWeatherResponse, newItem: FavoriteWeatherResponse): Boolean {
        return oldItem == newItem
    }
}