package com.example.weathermate.weather_data_fetcher

import com.google.gson.annotations.SerializedName

data class WeatherAPIResponse(
    @SerializedName("lat") private val cityLatitude: Double,
    @SerializedName("lon") private val cityLongitude: Double,
    @SerializedName("timezone") private val locationName : String,
    private val timezone_offset : Int,
    @SerializedName("current") private val currentForecast : CurrentForecast,//now
    @SerializedName("hourly") private val hourlyForecast : List<HourlyForecast>,
    @SerializedName("daily") private val dailyForecast : List<DailyForecast>
)
