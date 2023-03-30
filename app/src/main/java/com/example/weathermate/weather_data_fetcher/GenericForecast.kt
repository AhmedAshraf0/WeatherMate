package com.example.weathermate.weather_data_fetcher

import androidx.databinding.InverseMethod
import com.google.gson.annotations.SerializedName

//Generic means hourly or current or daily - forecast

data class CurrentForecast(
    @SerializedName("dt") var time: Long,
    var sunrise: Long,
    var sunset: Long,
    var temp: Double,
    @SerializedName("feels_like") var feelsLike: Double,
    var pressure: Int,
    var humidity: Int,
    @SerializedName("dew_point") var atmosphericTemp: Double,
    var uvi: Double,
    var clouds: Int,
    var visibility: Int,
    @SerializedName("wind_speed") var windSpeed: Double,
    @SerializedName("wind_gust") var windGust: Double?,
    @SerializedName("wind_deg") var windDeg: Int,
    var rain: WeatherStat?,
    var snow: WeatherStat?,
    var weather: List<CurrentWeather>
)

data class WeatherStat(//rain - snow
    @SerializedName("1h") var precipitation: Double
)

data class CurrentWeather(
    var id: Int,
    var main: String,
    var description: String,
    var icon: String//icon : 04d
)

data class HourlyForecast(
    @SerializedName("dt") var time: Long,
    var temp: Double,
    @SerializedName("feels_like") var feelsLike: Double,
    var pressure: Int,
    var humidity: Int,
    @SerializedName("dew_point") var atmosphericTemp: Double,
    var uvi: Double,
    var clouds: Int,
    var visibility: Int,
    @SerializedName("wind_speed") var windSpeed: Double,
    @SerializedName("wind_gust") var windGust: Double?,
    @SerializedName("wind_deg") var windDeg: Int,
    @SerializedName("pop") var percentOfProbability: Double,
    var rain: WeatherStat?,
    var snow: WeatherStat?,
    var weather: List<CurrentWeather>
)

data class TempDetails(
    var day: Double,
    var min: Double,
    var max: Double,
    var night: Double,
    var eve: Double,
    var morn: Double,
)

data class FeelLikeDetails(
    var day: Double,
    var night: Double,
    var eve: Double,
    var morn: Double,
)

data class DailyForecast(
    @SerializedName("dt") var time: Long,
    var sunrise: Long,
    var sunset: Long,
    var moonrise: Long,
    var moonset: Long,
    var moon_phase: Double,
    var temp: TempDetails,
    @SerializedName("feels_like") var feelsLike: FeelLikeDetails,
    var pressure: Int,
    var humidity: Int,
    @SerializedName("dew_point") var atmosphericTemp: Double,
    var uvi: Double,
    @SerializedName("pop") var percentOfProbability: Double,
    var clouds: Int,
    @SerializedName("wind_speed") var windSpeed: Double,
    @SerializedName("wind_gust") var windGust: Double,
    @SerializedName("wind_deg") var windDeg: Int,
    var rain: Double?,
    var snow: Double?,
    var weather: List<CurrentWeather>
)