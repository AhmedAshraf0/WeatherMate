package com.example.weathermate.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weathermate.weather_data_fetcher.CurrentForecast
import com.example.weathermate.weather_data_fetcher.CurrentWeather
import com.example.weathermate.weather_data_fetcher.WeatherResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.`is`
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
@ExperimentalCoroutinesApi
class WeatherDaoTest {
    private lateinit var database : WeatherDB
    private lateinit var dao : WeatherDao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDB::class.java
        ).allowMainThreadQueries().build()

        dao = database.getWeatherDao()
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun insertWeatherDetails_weatherResponse_OneInserted() = runBlockingTest{
        //sets up the object to insert
        val weatherResponse = WeatherResponse(
            id = 1,
            cityLatitude = 40.7128,
            cityLongitude = -74.0060,
            locationName = "New York",
            timezone_offset = -14400,
            currentForecast = CurrentForecast(
                time = 1618242884,
                sunrise = 1618206314,
                sunset = 1618251784,
                temp = 17.0,
                feelsLike = 16.0,
                pressure = 1012,
                humidity = 70,
                atmosphericTemp = 10.0,
                uvi = 0.0,
                clouds = 20,
                visibility = 10000,
                windSpeed = 3.6,
                windGust = null,
                windDeg = 150,
                rain = null,
                snow = null,
                weather = listOf(
                    CurrentWeather(
                    id = 800,
                    main = "Clear",
                    description = "clear sky",
                    icon = "01d"
                )
                )
            ),
            hourlyForecast = listOf(),
            dailyForecast = listOf()
        )

        dao.insertWeatherDetails(weatherResponse)

        //when getting list of data from room
        var allWeatherDetailsList = dao.getLocalWeatherDetails()

        //then testing that list returned from room has one item
        assertThat(allWeatherDetailsList.get(0),`is`(weatherResponse) )
    }
}