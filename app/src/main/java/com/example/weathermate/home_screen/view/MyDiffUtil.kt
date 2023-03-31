package com.example.weathermate.home_screen.view

import androidx.recyclerview.widget.DiffUtil
import com.example.weathermate.weather_data_fetcher.HourlyForecast

class MyDiffUtil : DiffUtil.ItemCallback<HourlyForecast>() {
    override fun areItemsTheSame(oldItem: HourlyForecast, newItem: HourlyForecast): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: HourlyForecast, newItem: HourlyForecast): Boolean {
        return oldItem == newItem
    }
}