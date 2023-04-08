package com.example.weathermate.favorites.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathermate.database.DbState
import com.example.weathermate.network.ApiState
import com.example.weathermate.favorites.model.FavoriteRepositoryInterface
import com.example.weathermate.weather_data_fetcher.FavoriteWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val _repo: FavoriteRepositoryInterface
): ViewModel() {
    private val TAG = "FavoriteViewModel"

    private var _retrofitStateFlow = MutableStateFlow<ApiState>(ApiState.Loading)
    var retrofitStateFlow: MutableStateFlow<ApiState> = _retrofitStateFlow

    private var _roomStateFlow = MutableStateFlow<DbState>(DbState.Loading)
    var roomStateFlow: MutableStateFlow<DbState> = _roomStateFlow

    fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        units: String,
        lang: String,
        exclude: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getWeatherData(
                latitude,
                longitude,
                units,
                lang,
                exclude
            ).catch {
                _retrofitStateFlow.value = ApiState.Failure(it)
                Log.i(TAG, "getWeatherDetails: ${it.message}")
            }.collectLatest {
                if (it.isSuccessful) {
                    _retrofitStateFlow.value = ApiState.Success(it.body()!!)
                    Log.i(TAG, "getWeatherDetails: ${it.body()!!.timezone_offset}")
                } else {
                    Log.i(TAG, "getWeatherDetails: failed ${it.errorBody().toString()}")
                }
            }
        }
    }

    fun getLocalFavDetails() {
        Log.i(TAG, "getLocalFavDetails: ")
        viewModelScope.launch(Dispatchers.IO) {
            _repo.getLocalFavDetails().catch{
                _roomStateFlow.value = DbState.Failure(it)
                Log.i(TAG, "getWeatherDetails-room: ${it.printStackTrace()}")
            }.collectLatest{
                _roomStateFlow.value = DbState.Success(favoriteWeatherResponse = it)
                Log.i(TAG, "getLocalFavDetails: success ${it.size}")
            }
        }
    }

    fun insertNewFavorite(favoriteWeatherResponse: FavoriteWeatherResponse){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.insertNewFavorite(favoriteWeatherResponse)
        }
    }

    fun deleteFavorite(favoriteWeatherResponse: FavoriteWeatherResponse){
        viewModelScope.launch(Dispatchers.IO) {
            _repo.deleteFavorite(favoriteWeatherResponse)
        }
    }

}