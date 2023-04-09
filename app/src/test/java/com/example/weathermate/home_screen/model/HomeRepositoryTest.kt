package com.example.weathermate.home_screen.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.weathermate.database.FakeLocalDataSource
import com.example.weathermate.database.LocalSource
import com.example.weathermate.database.WeatherDB
import com.example.weathermate.network.FakeRemoteDataSource
import com.example.weathermate.network.RemoteSource
import com.example.weathermate.weather_data_fetcher.WeatherResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class HomeRepositoryTest {
    lateinit var localSource: LocalSource
    lateinit var remoteSource: RemoteSource
    lateinit var repository: HomeRepositoryInterface

    @Before
    fun setup() {
        localSource = FakeLocalDataSource(
            mutableListOf(
                WeatherResponse(1),
                WeatherResponse(2),
                WeatherResponse(3)
            )
        )
        remoteSource = FakeRemoteDataSource()
        repository = FakeHomeRepository(localSource, remoteSource)
    }

    @Test
    fun addNewWeatherDetails_newWeatherDetails_sizeFour() = runBlockingTest {
        //when prepare new item and inserting it
        val weatherResponse4 = WeatherResponse(4)
        repository.insertWeatherDetails(weatherResponse4)

        //given get the new list
        val list = repository.getLocalWeatherDetails().first()

        //then last item is the new item
        assertThat(list.last().id, `is`(4))
    }

    @Test
    fun getWeatherDetailsList_zero_sizeThree() = runBlockingTest {
        //when
        val list = repository.getLocalWeatherDetails().first()

        //then
        assertThat(list.size, `is`(3))
    }

    @Test
    fun getOnlineWeatherDetails_latLong_weatherResponse() = runBlockingTest {
        //passing data and saving the response
        val weatherResponse = repository.getWeatherData(
            31.1, 23.1,
            "metric", "en"
        ).first()

        //when
        assertThat(weatherResponse.body()?.cityLatitude,`is`(31.1))
        assertThat(weatherResponse.body()?.cityLongitude,`is`(23.1))
    }

}