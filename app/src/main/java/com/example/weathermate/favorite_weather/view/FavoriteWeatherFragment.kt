package com.example.weathermate.favorite_weather.view


import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.weathermate.database.ConcreteLocalSource
import com.example.weathermate.databinding.FragmentFavoriteWeatherBinding
import com.example.weathermate.home_screen.model.HomeRepository
import com.example.weathermate.home_screen.model.photos
import com.example.weathermate.home_screen.view.DailyAdapter
import com.example.weathermate.home_screen.view.HourlyAdapter
import com.example.weathermate.home_screen.viewmodel.HomeViewModel
import com.example.weathermate.home_screen.viewmodel.HomeViewModelFactory
import com.example.weathermate.network.ApiState
import com.example.weathermate.network.ConcreteRemoteSource
import com.example.weathermate.weather_data_fetcher.WeatherResponse
import kotlinx.coroutines.flow.collectLatest
import java.io.IOException
import java.util.*

class FavoriteWeatherFragment : Fragment() {

    private val TAG = "FavoriteWeatherFragment"
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var _binding: FragmentFavoriteWeatherBinding

    private lateinit var favoriteWeatherVm: HomeViewModel
    private lateinit var factory: HomeViewModelFactory

    private lateinit var hourlyAdapter: HourlyAdapter
    private lateinit var dailyAdapter: DailyAdapter

    val args: FavoriteWeatherFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "onCreateView: ")
        _binding = FragmentFavoriteWeatherBinding.inflate(inflater)

        sharedPreferences = getSharedPreferences(requireActivity())
        editor = sharedPreferences.edit()

        hourlyAdapter = HourlyAdapter()
        dailyAdapter = DailyAdapter()

        factory =
            HomeViewModelFactory(
                HomeRepository.getInstance(
                    ConcreteRemoteSource(),
                    ConcreteLocalSource(requireContext())
                )
            )

        favoriteWeatherVm = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        if (checkForInternet(requireContext())) {
            Log.i(TAG, "checkForInternet: yes")
            getWeatherDetails(
                args.latlong.split(",").get(0).toDouble(),
                args.latlong.split(",").get(1).toDouble(),
                sharedPreferences.getString("units","metric")!!,
                sharedPreferences.getString("lang","en")!!
            )
        }
        return _binding.root
    }

    private fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        units: String,
        lang: String
    ) {

        favoriteWeatherVm.getWeatherDetails(latitude, longitude, units, lang)
        //Start Consuming
        lifecycleScope.launchWhenResumed {
            favoriteWeatherVm.retrofitStateFlow.collectLatest {
                when (it) {
                    is ApiState.Success -> {
                        //i want to make sure that user received data at least once
                        //to avoid errors
                        Log.i(
                            TAG,
                            "getWeatherDetails: ${it.data.locationName}${it.data.cityLatitude}${it.data.cityLongitude}"
                        )

                        _binding.weatherApiResponse = it.data

                        try {
                            val geocoder = Geocoder(requireContext(), Locale.getDefault())
                            val address = geocoder.getFromLocation(
                                it.data.cityLatitude,
                                it.data.cityLongitude,
                                1
                            ) as List<Address>
                            //swap location from api to location from geocoder
                            try{
                                if(address.get(0).getAddressLine(0).split(",").size > 1){
                                    it.data.locationName = address.get(0).getAddressLine(0).split(",").get(1)
                                }else{
                                    it.data.locationName = address.get(0).getAddressLine(0).split(",").get(0)
                                }
                            }catch (e: IndexOutOfBoundsException){
                                it.data.locationName = "-"
                            }
                        }catch (e: IOException){
                            it.data.locationName = "-"
                            Log.i(TAG, "getWeatherDetails: failed")
                        }


                        if (getSharedPreferences(requireActivity()).getString("lang", "en")
                                .equals("en")
                        ) {
                            try {
                                _binding.tvCurrentLocation.text =
                                    it.data.locationName.split(",").get(1)
                            } catch (e: IndexOutOfBoundsException) {

                                _binding.tvCurrentLocation.text = it.data.locationName
                            }
                        }

                        onResponseState(it.data)
                    }
                    is ApiState.Loading -> {
                        Log.i(TAG, "getWeatherDetails: loading")
                        _binding.progressBar.playAnimation()
                        _binding.progressBar.visibility = View.VISIBLE
                        _binding.mainGroup.visibility = View.GONE
                    }
                    is ApiState.Failure -> {
                        //---visiblity of whole layout gone and show error msg

                        _binding.progressBar.pauseAnimation()
                        Log.i(TAG, "getWeatherDetails: error: ${it.msg.printStackTrace()}")
                    }
                }
            }
        }

    }

    private fun checkForInternet(context: Context): Boolean {
        //connectivityManager to get system services so i can know network connections
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
    }

    private fun onResponseState(weatherResponse: WeatherResponse) {
        Log.i(TAG, "getWeatherDetails: ")
        _binding.todayImg.setImageResource(
            photos.get(
                weatherResponse.currentForecast!!.weather.get(
                    0
                ).icon
            )!!
        )

        hourlyAdapter.submitList(weatherResponse.hourlyForecast.take(24))
        _binding.recHourly.adapter = hourlyAdapter

        dailyAdapter.submitList(weatherResponse.dailyForecast.drop(1).take(7))
        _binding.recNextDays.adapter = dailyAdapter

        _binding.progressBar.visibility = View.GONE
        _binding.mainGroup.visibility = View.VISIBLE
        _binding.progressBar.pauseAnimation()
    }
}