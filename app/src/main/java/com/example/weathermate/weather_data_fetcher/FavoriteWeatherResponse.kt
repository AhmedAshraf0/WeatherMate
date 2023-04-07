package com.example.weathermate.weather_data_fetcher

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("favorites",primaryKeys = ["id"])
data class FavoriteWeatherResponse(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var id: Int = 0,
    @ColumnInfo
    var latitude: Double = 0.0,
    @ColumnInfo
    var longitude: Double = 0.0,
    @ColumnInfo
    var cityName: String = "",
    @ColumnInfo
    var temp: Double = 0.0,
    @ColumnInfo
    var dt: Long = 0L,
    @ColumnInfo
    var img: String = ""
)
