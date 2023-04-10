package com.example.weathermate.home_screen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathermate.database.DbState
import com.example.weathermate.home_screen.model.HomeRepositoryInterface
import com.example.weathermate.network.ApiState
import com.example.weathermate.weather_data_fetcher.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val _repo: HomeRepositoryInterface,
) : ViewModel() {
    private val TAG = "HomeViewModel"

    private var _retrofitStateFlow = MutableStateFlow<ApiState>(ApiState.Loading)
    var retrofitStateFlow: MutableStateFlow<ApiState> = _retrofitStateFlow

    private var _roomStateFlow = MutableStateFlow<DbState>(DbState.Loading)
    var roomStateFlow: MutableStateFlow<DbState> = _roomStateFlow

    fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        units: String,
        lang: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getWeatherData(
                latitude,
                longitude,
                units,
                lang
            ).catch {
                _retrofitStateFlow.value = ApiState.Failure(it)
                Log.i(TAG, "getWeatherDetails: ${it.message}")
            }.collect {
                if (it.isSuccessful) {
                    _retrofitStateFlow.value = ApiState.Success(it.body()!!)
                } else {
                    Log.i(TAG, "getWeatherDetails: failed ${it.errorBody().toString()}")
                }
            }
        }
    }

    fun getLocalWeatherDetails(){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getLocalWeatherDetails().catch{
                _roomStateFlow.value = DbState.Failure(it)
                Log.i(TAG, "getWeatherDetails-room: ${it.printStackTrace()}")
            }.collect{
                _roomStateFlow.value = DbState.Success(it.get(0))
            }
        }
    }

    fun insertWeatherDetails(weatherResponse: WeatherResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.insertWeatherDetails(weatherResponse)
        }
    }

    /*fun updateWeatherDetails(weatherResponse: WeatherResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.updateWeatherDetails(weatherResponse)
        }
    }*/

}