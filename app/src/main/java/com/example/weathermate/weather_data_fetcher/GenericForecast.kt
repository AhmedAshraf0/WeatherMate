package com.example.weathermate.weather_data_fetcher

import com.google.gson.annotations.SerializedName

//Generic means hourly or current or daily - forecast

data class CurrentForecast(
    @SerializedName("dt") private val time: Long,
    private val sunrise: Long,
    private val sunset: Long,
    private val temp: Double,
    @SerializedName("feels_like") private val feelsLike: Double,
    private val pressure: Int,
    private val humidity: Int,
    @SerializedName("dew_point") private val atmosphericTemp: Double,
    private val uvi: Double,
    private val clouds: Int,
    private val visibility: Int,
    @SerializedName("wind_speed") private val windSpeed: Double,
    @SerializedName("wind_gust") private val windGust: Double,
    @SerializedName("wind_deg") private val windDeg: Int,
    private val rain: WeatherStat,
    private val snow: WeatherStat,
    private val weather: List<CurrentWeather>
)

data class WeatherStat(//rain - snow
    @SerializedName("1h") private val precipitation: Double
)

data class CurrentWeather(
    private val id: Int,
    private val main: String,
    private val description: String,
    private val icon: String//icon : 04d
)

data class HourlyForecast(
    @SerializedName("dt") private val time: Long,
    private val temp: Double,
    @SerializedName("feels_like") private val feelsLike: Double,
    private val pressure: Int,
    private val humidity: Int,
    @SerializedName("dew_point") private val atmosphericTemp: Double,
    private val uvi: Double,
    private val clouds: Int,
    private val visibility: Int,
    @SerializedName("wind_speed") private val windSpeed: Double,
    @SerializedName("wind_gust") private val windGust: Double,
    @SerializedName("wind_deg") private val windDeg: Int,
    @SerializedName("pop") private val percentOfProbability: Double,
    private val rain: WeatherStat,
    private val snow: WeatherStat,
    private val weather: List<CurrentWeather>
)

data class TempDetails(
    private val day: Double,
    private val min: Double,
    private val max: Double,
    private val night: Double,
    private val eve: Double,
    private val morn: Double,
)

data class FeelLikeDetails(
    private val day: Double,
    private val night: Double,
    private val eve: Double,
    private val morn: Double,
)

data class DailyForecast(
    @SerializedName("dt") private val time: Long,
    private val sunrise: Long,
    private val sunset: Long,
    private val moonrise: Long,
    private val moonset: Long,
    private val moon_phase: Double,
    private val temp: TempDetails,
    @SerializedName("feels_like") private val feelsLike: FeelLikeDetails,
    private val pressure: Int,
    private val humidity: Int,
    @SerializedName("dew_point") private val atmosphericTemp: Double,
    private val uvi: Double,
    @SerializedName("pop") private val percentOfProbability: Double,
    private val clouds: Int,
    @SerializedName("wind_speed") private val windSpeed: Double,
    @SerializedName("wind_gust") private val windGust: Double,
    @SerializedName("wind_deg") private val windDeg: Int,
    private val rain: Double,
    private val snow: Double,
    private val weather: List<CurrentWeather>
)