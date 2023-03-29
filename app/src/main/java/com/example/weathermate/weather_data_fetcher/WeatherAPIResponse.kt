package com.example.weathermate.weather_data_fetcher

import com.google.gson.annotations.SerializedName

data class WeatherAPIResponse(
    @SerializedName("lat") val cityLatitude: Double,
    @SerializedName("lon") val cityLongitude: Double,
    @SerializedName("timezone") val locationName : String,
    val timezone_offset : Int,
    @SerializedName("current") val currentForecast : CurrentForecast,//now
    @SerializedName("hourly") val hourlyForecast : List<HourlyForecast>,
    @SerializedName("daily") val dailyForecast : List<DailyForecast>
)
