package com.example.weathermate.weather_data_fetcher

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import com.example.weathermate.database.*
import com.example.weathermate.utilities.*
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity("weather",primaryKeys = ["id"])
@TypeConverters(
    CurrentForecastConverter::class,
    CurrentWeatherListConverter::class,
    WeatherStatConverter::class,
    HourlyListConverter::class,
    TempDetailsConverter::class,
    FeelDetailsConverter::class,
    DailyListConverter::class
)
//calling all the converters here and room will handle if a non primitive type object
//has another non primitive type object inside it. Room will manipulate this.
data class WeatherResponse(
    var id: Int = 1,

    @ColumnInfo
    @SerializedName("lat")
    var cityLatitude: Double = 0.0,

    @ColumnInfo
    @SerializedName("lon")
    var cityLongitude: Double = 0.0,

    @ColumnInfo
    @SerializedName("timezone")
    var locationName: String = "",

    @ColumnInfo
    var timezone_offset: Int = 0,

    @ColumnInfo
    @SerializedName("current")
    var currentForecast: CurrentForecast? = null,//now

    @ColumnInfo
    @SerializedName("hourly")
    var hourlyForecast: List<HourlyForecast> = listOf(),

    @ColumnInfo
    @SerializedName("daily")
    var dailyForecast: List<DailyForecast> = listOf()
) : Serializable
