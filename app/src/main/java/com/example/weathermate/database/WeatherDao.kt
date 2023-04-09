package com.example.weathermate.database

import androidx.room.*
import com.example.weathermate.weather_data_fetcher.FavoriteWeatherResponse
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

    @Query("SELECT * FROM favorites")
    fun getLocalFavDetails() : List<FavoriteWeatherResponse>

    @Insert
    suspend fun insertNewFavorite(favoriteWeatherResponse: FavoriteWeatherResponse)

    @Delete
    suspend fun deleteFavorite(favoriteWeatherResponse: FavoriteWeatherResponse)
}
