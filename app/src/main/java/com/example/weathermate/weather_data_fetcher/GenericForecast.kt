package com.example.weathermate.weather_data_fetcher

import com.google.gson.annotations.SerializedName

//Generic means hourly or current or daily - forecast

data class CurrentForecast(
    @SerializedName("dt") val time: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    @SerializedName("dew_point") val atmosphericTemp: Double,
    val uvi: Double,
    val clouds: Int,
    val visibility: Int,
    @SerializedName("wind_speed") val windSpeed: Double,
    @SerializedName("wind_gust") val windGust: Double?,
    @SerializedName("wind_deg") val windDeg: Int,
    val rain: WeatherStat?,
    val snow: WeatherStat?,
    val weather: List<CurrentWeather>
)

data class WeatherStat(//rain - snow
    @SerializedName("1h") val precipitation: Double
)

data class CurrentWeather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String//icon : 04d
)

data class HourlyForecast(
    @SerializedName("dt") val time: Long,
    val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    @SerializedName("dew_point") val atmosphericTemp: Double,
    val uvi: Double,
    val clouds: Int,
    val visibility: Int,
    @SerializedName("wind_speed") val windSpeed: Double,
    @SerializedName("wind_gust") val windGust: Double?,
    @SerializedName("wind_deg") val windDeg: Int,
    @SerializedName("pop") val percentOfProbability: Double,
    val rain: WeatherStat?,
    val snow: WeatherStat?,
    val weather: List<CurrentWeather>
)

data class TempDetails(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double,
)

data class FeelLikeDetails(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double,
)

data class DailyForecast(
    @SerializedName("dt") val time: Long,
    val sunrise: Long,
    val sunset: Long,
    val moonrise: Long,
    val moonset: Long,
    val moon_phase: Double,
    val temp: TempDetails,
    @SerializedName("feels_like") val feelsLike: FeelLikeDetails,
    val pressure: Int,
    val humidity: Int,
    @SerializedName("dew_point") val atmosphericTemp: Double,
    val uvi: Double,
    @SerializedName("pop") val percentOfProbability: Double,
    val clouds: Int,
    @SerializedName("wind_speed") val windSpeed: Double,
    @SerializedName("wind_gust") val windGust: Double,
    @SerializedName("wind_deg") val windDeg: Int,
    val rain: Double?,
    val snow: Double?,
    val weather: List<CurrentWeather>
)