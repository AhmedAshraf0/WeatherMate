package com.example.weathermate.database

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weathermate.weather_data_fetcher.CurrentForecast
import com.example.weathermate.weather_data_fetcher.CurrentWeather
import com.example.weathermate.weather_data_fetcher.FavoriteWeatherResponse
import com.example.weathermate.weather_data_fetcher.WeatherResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.`is`
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.equalTo
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
        assertThat(allWeatherDetailsList.get(0).locationName,`is`(weatherResponse.locationName) )
        assertThat(allWeatherDetailsList.get(0).currentForecast?.feelsLike,`is`(weatherResponse.currentForecast?.feelsLike) )
    }

    @Test
    fun insertSameWeatherDetailsTwice_weatherResponse_firstOneIsReplacedWithSecondOne() = runBlockingTest{
        //sets up the 2 object to insert
        val weatherResponse1 = WeatherResponse(
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
        val weatherResponse2 = WeatherResponse(
            id = 1,
            cityLatitude = 40.7128,
            cityLongitude = -74.0060,
            locationName = "Alex",
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

        dao.insertWeatherDetails(weatherResponse1)
        dao.insertWeatherDetails(weatherResponse2)

        //when getting list of data from room
        var allWeatherDetailsList = dao.getLocalWeatherDetails()

        //then testing that list returned from room has only the second weatherResponse
        assertThat(allWeatherDetailsList.get(0).locationName,`is`("Alex") )
    }

    @Test
    fun insertFavoriteWeatherResponse_favoriteWeatherResponse_OneInserted() = runBlockingTest{
        //sets up the 1 object to insert
        val favoriteWeatherResponse = FavoriteWeatherResponse(
            latitude = 40.7128,
            longitude = -74.0060,
            cityName = "New York",
            temp = 17.0,
            dt = 1618242884L,
            img = "01d",
            description = "clear sky"
        )


        dao.insertNewFavorite(favoriteWeatherResponse)

        //when getting list of data from room
        val allWeatherDetailsList = dao.getLocalFavDetails()

        //then testing that list returned from room has only the second weatherResponse
        assertThat(allWeatherDetailsList.get(0).description,`is`("clear sky") )
    }

    /*@Test
    fun insertFavoriteWeatherResponseAndDeleteIt_favoriteWeatherResponse_zeroItemsInList() = runBlockingTest{
        //sets up the 1 object to insert
        val favoriteWeatherResponse = FavoriteWeatherResponse(
            latitude = 40.7128,
            longitude = -74.0060,
            cityName = "New York",
            temp = 17.0,
            dt = 1618242884L,
            img = "01d",
            description = "clear sky"
        )


        dao.insertNewFavorite(favoriteWeatherResponse)

        //when getting list of data from room
        dao.deleteFavorite(favoriteWeatherResponse)

        val allFavs = dao.getLocalFavDetails()

        //then testing that list returned zero
        Log.i(
            "TAG",
            "insertFavoriteWeatherResponseAndDeleteIt_favoriteWeatherResponse_zeroItemsInList: ${allFavs.size}"
        )
        assertThat(allFavs.size, equalTo(0) )
    }*/
}