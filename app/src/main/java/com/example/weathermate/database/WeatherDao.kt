package com.example.weathermate.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.weathermate.weather_data_fetcher.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("Select * from weather")
    fun getLocalWeatherDetails() : Flow<List<WeatherResponse>>

    @Insert
    suspend fun insertWeatherDetails(weatherResponse: WeatherResponse)

    @Update
    suspend fun updateWeatherDetails(weatherResponse: WeatherResponse) : Int
}
