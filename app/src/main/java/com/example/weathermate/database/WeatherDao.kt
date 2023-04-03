package com.example.weathermate.database

import androidx.room.*
import com.example.weathermate.weather_data_fetcher.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather")
    fun getLocalWeatherDetails() : List<WeatherResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeatherDetails(weatherResponse: WeatherResponse)

    @Update
    suspend fun updateWeatherDetails(weatherResponse: WeatherResponse) : Int
}
