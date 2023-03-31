package com.example.weathermate.home_screen.view

import androidx.recyclerview.widget.DiffUtil
import com.example.weathermate.weather_data_fetcher.DailyForecast
import com.example.weathermate.weather_data_fetcher.HourlyForecast

class DiffUtilDaily : DiffUtil.ItemCallback<DailyForecast>() {
    override fun areItemsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
        return oldItem == newItem
    }
}