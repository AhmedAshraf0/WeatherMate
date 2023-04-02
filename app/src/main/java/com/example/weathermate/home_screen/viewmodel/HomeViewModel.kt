package com.example.weathermate.home_screen.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathermate.home_screen.model.HomeRepositoryInterface
import com.example.weathermate.network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(
    private val _repo: HomeRepositoryInterface,
    latitude: Double,
    longitude: Double,
    units: String,
    lang: String
) : ViewModel() {
    private val TAG = "HomeViewModel"

    private var _responseStateFlow = MutableStateFlow<ApiState>(ApiState.Loading)
    var responseStateFlow: MutableStateFlow<ApiState> = _responseStateFlow

    init {
        getWeatherDetails(latitude, longitude, units, lang)
    }

    private fun getWeatherDetails(
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
                _responseStateFlow.value = ApiState.Failure(it)
                Log.i(TAG, "getWeatherDetails: ${it.message}")
            }.collect {
                if (it.isSuccessful) {
                    _responseStateFlow.value = ApiState.Success(it.body()!!)
                } else {
                    Log.i(TAG, "getWeatherDetails: failed ${it.errorBody().toString()}")
                }
            }
        }
    }

}