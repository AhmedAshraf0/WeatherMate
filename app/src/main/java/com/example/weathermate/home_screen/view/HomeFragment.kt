package com.example.weathermate.home_screen.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weathermate.databinding.FragmentHomeBinding
import com.example.weathermate.home_screen.model.HomeRepository
import com.example.weathermate.home_screen.viewmodel.HomeViewModel
import com.example.weathermate.home_screen.viewmodel.HomeViewModelFactory
import com.example.weathermate.network.ApiState
import com.example.weathermate.network.ConcreteRemoteSource
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.collectLatest

class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    private val PERMISSION_ID = 10
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var _binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var factory: HomeViewModelFactory
    private lateinit var hourlyAdapter: HourlyAdapter
    private lateinit var dailyAdapter: DailyAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater)

        hourlyAdapter = HourlyAdapter()
        dailyAdapter = DailyAdapter()

        getLocation()

        return _binding.root
    }


    private fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        units: String,
        lang: String
    ) {
        factory =
            HomeViewModelFactory(
                HomeRepository.getInstance(ConcreteRemoteSource()),
                latitude,
                longitude,
                units,
                lang
            )

        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        //Start Consuming
        lifecycleScope.launchWhenResumed {
            homeViewModel.responseStateFlow.collectLatest {
                when (it) {
                    is ApiState.Success -> {
                        Log.i(TAG, "getWeatherDetails: ${it.data.locationName}")

                        _binding.weatherApiResponse = it.data

                        hourlyAdapter.submitList(it.data.hourlyForecast.take(24))
                        _binding.recHourly.adapter = hourlyAdapter

                        dailyAdapter.submitList(it.data.dailyForecast.drop(0).take(7))
                        _binding.recNextDays.adapter = dailyAdapter

                        _binding.progressBar.visibility = View.GONE
                        _binding.mainGroup.visibility = View.VISIBLE
                    }
                    is ApiState.Loading -> {
                        _binding.progressBar.visibility = View.VISIBLE
                        _binding.mainGroup.visibility = View.GONE
                    }
                    else -> {
                        //visiblity of whole layout gone and show error msg
                        Log.i(TAG, "getWeatherDetails: error")
                    }
                }
            }
        }
    }

    private fun isLocationEnable(): Boolean {
        //reserve reference of location manager
        //condition could be modified in any case i want
        val locationManager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun getLocation() {
        if (checkPermissions()) {//if permissions available after granting them from the user
            if (isLocationEnable()) {//if any location is available
                requestNewLocationData()
            } else {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        } else {
            requestPermissions()
        }
    }

    @Suppress("MissingPermission")
    private fun requestNewLocationData() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY, null
        ).addOnSuccessListener { location: Location? ->
            if (location == null)
                Toast.makeText(requireContext(), "Cannot get location.", Toast.LENGTH_SHORT)
                    .show()
            else {
                getWeatherDetails(location.latitude, location.longitude, "metric", "en")
            }
        }
    }

    private fun requestPermissions() {
        //define permissions i want to check and custom unique permission id
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}