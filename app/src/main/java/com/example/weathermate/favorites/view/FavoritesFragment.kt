package com.example.weathermate.favorites.view

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.weathermate.R
import com.example.weathermate.database.ConcreteLocalSource
import com.example.weathermate.database.DbState
import com.example.weathermate.database.WeatherDB
import com.example.weathermate.databinding.FragmentFavoritesBinding
import com.example.weathermate.favorites.model.FavoriteRepository
import com.example.weathermate.favorites.viewmodel.FavoriteViewModel
import com.example.weathermate.favorites.viewmodel.FavoriteViewModelFactory
import com.example.weathermate.map.MapFragment
import com.example.weathermate.network.ApiState
import com.example.weathermate.network.ConcreteRemoteSource
import com.example.weathermate.weather_data_fetcher.FavoriteWeatherResponse
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

class FavoritesFragment : Fragment() {
    private val TAG = "FavoritesFragment"
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var _binding: FragmentFavoritesBinding

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var factory: FavoriteViewModelFactory
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater)
        _binding.myFragment = this

        sharedPreferences = getSharedPreferences(requireActivity())
        editor = sharedPreferences.edit()

        factory =
            FavoriteViewModelFactory(
                FavoriteRepository.getInstance(
                    ConcreteRemoteSource(),
                    ConcreteLocalSource(WeatherDB.getInstance(requireContext()).getWeatherDao())
                )
            )

        favoriteViewModel = ViewModelProvider(this,factory).get(FavoriteViewModel::class.java)

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args : FavoritesFragmentArgs by navArgs()
        favoritesAdapter = FavoritesAdapter(favoriteViewModel,requireActivity(),view)

        _binding.rvFavs.adapter = favoritesAdapter

        Log.i(TAG, "onCreateView:  ${args.locationLatLong.length}")
        Log.i(
            TAG,
            "stack: ${findNavController().previousBackStackEntry?.destination?.id}\n${R.id.mapFragment}"
        )
        if(args.locationLatLong != "hi" && MapFragment.isFromMap){
            if(checkForInternet(requireContext()) && checkPermissions()){
                MapFragment.isFromMap =false
                getWeatherDetails(
                    args.locationLatLong.split(",").get(0).toDouble(),
                    args.locationLatLong.split(",").get(1).toDouble(),
                    sharedPreferences.getString("units","metric")!!,
                    sharedPreferences.getString("lang","en")!!,
                    "minutely,hourly,daily,alerts"
                )
            }else{
                Snackbar.make(view, "Check internet & GPS", Snackbar.LENGTH_LONG).show()
            }
        }
        favoriteViewModel.getLocalFavDetails()

        getLocalWeatherDetails()
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
    }

    fun onFabClicked(view: View){
        if(checkForInternet(requireContext())){
            val navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_content_main)
            val action = FavoritesFragmentDirections.actionNavFavsToMapFragment(false)
            navController.navigate(action)
        }else{
            //no internet
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

    private fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        units: String,
        lang: String,
        exclude: String
    ) {
        favoriteViewModel.getWeatherDetails(
            latitude,
            longitude, units, lang, exclude
        )

        lifecycleScope.launchWhenResumed {
            favoriteViewModel.retrofitStateFlow.collect {
                when(it){
                    is ApiState.Success ->{
                        Log.i(TAG, "getWeatherDetails:apisuccess ${it.data.currentForecast!!.temp}")

                        //get the complete address
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
                                Log.i(TAG, "getWeatherDetails: ${address}")
                                it.data.locationName = "-"
                            }
                        }catch (e: IOException){
                            it.data.locationName = "-"
                            Log.i(TAG, "getWeatherDetails: failed")
                        }
                        val favoriteWeatherResponse = FavoriteWeatherResponse(
                            latitude = latitude,
                            longitude = longitude,
                            cityName = it.data.locationName,
                            description = it.data.currentForecast!!.weather.get(0).description,
                            dt = it.data.currentForecast!!.time,
                            temp = it.data.currentForecast!!.temp,
                            img = it.data.currentForecast!!.weather.get(0).icon
                        )
                        favoriteViewModel.insertNewFavorite(favoriteWeatherResponse)
                    }
                    is ApiState.Loading ->{
                        Log.i(TAG, "getWeatherDetails: loading")
                        _binding.progressBar.playAnimation()
                        _binding.progressBar.visibility = View.VISIBLE
                        _binding.mainGroup.visibility = View.GONE
                    }
                    is ApiState.Failure ->{
                        _binding.progressBar.pauseAnimation()
                        Log.i(TAG, "getWeatherDetails: error: ${it.msg.printStackTrace()}")
                    }
                }
            }
        }
    }

    private fun getLocalWeatherDetails(){
        Log.i(TAG, "getLocalWeatherDetails: ")
        lifecycleScope.launchWhenResumed {
            favoriteViewModel.roomStateFlow.collect {
                when(it){
                    is DbState.Success ->{
                        Log.i(TAG, "getLocalWeatherDetails: ${it.favoriteWeatherResponse!!.size}")
                        if(it.favoriteWeatherResponse.isNotEmpty()){
                            favoritesAdapter.submitList(it.favoriteWeatherResponse)

                            _binding.progressBar.visibility = View.GONE
                            _binding.progressBar.pauseAnimation()
                            Log.i(TAG, "getLocalWeatherDetails: success in if")
                        }else{//we have no favs
                            Log.i(TAG, "getLocalWeatherDetails: success in else")
                            _binding.progressBar.visibility = View.GONE
                            _binding.progressBar.pauseAnimation()
                            favoritesAdapter.submitList(listOf())
                        }
                        _binding.mainGroup.visibility = View.VISIBLE
                    }
                    is DbState.Loading ->{
                        Log.i(TAG, "getLocalWeatherDetails db: loading")
                        _binding.progressBar.playAnimation()
                        _binding.progressBar.visibility = View.VISIBLE
                        _binding.mainGroup.visibility = View.GONE
                    }
                    is DbState.Failure ->{
                        _binding.progressBar.pauseAnimation()
                        Log.i(
                            TAG,
                            "getLocalWeatherDetails: Failed to get data from room ${it.msg.printStackTrace()}"
                        )
                    }
                }
                Log.i(TAG, "getLocalWeatherDetails: end")
            }
        }
        Log.i(TAG, "getLocalWeatherDetails: fun done")
    }

    private fun checkPermissions(): Boolean {
        Log.i(TAG, "checkPermissions: ")
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestPermissions() {
        Log.i(TAG, "requestPermissions: ")
        //define permissions i want to check and custom unique permission id

        //if from activity compat onRequestPermissionsResult is not called and i need it
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            10
        )
        //at this function the prompt is displayed and to check on the action of it
        //through this callback fun onRequestPermissionsResult()
    }
}