package com.example.weathermate.weather_data_fetcher

import com.google.gson.annotations.SerializedName

data class WeatherAPIResponse(
    @SerializedName("lat") var cityLatitude: Double,
    @SerializedName("lon") var cityLongitude: Double,
    @SerializedName("timezone") var locationName : String,
    var timezone_offset : Int,
    @SerializedName("current") var currentForecast : CurrentForecast,//now
    @SerializedName("hourly") var hourlyForecast : List<HourlyForecast>,
    @SerializedName("daily") var dailyForecast : List<DailyForecast>
)
