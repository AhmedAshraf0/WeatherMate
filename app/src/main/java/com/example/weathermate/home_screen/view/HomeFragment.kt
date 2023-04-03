package com.example.weathermate.home_screen.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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
import com.example.weathermate.database.ConcreteLocalSource
import com.example.weathermate.database.DbState
import com.example.weathermate.databinding.FragmentHomeBinding
import com.example.weathermate.dialog.MyDialogFragment
import com.example.weathermate.home_screen.model.HomeRepository
import com.example.weathermate.home_screen.model.photos
import com.example.weathermate.home_screen.viewmodel.HomeViewModel
import com.example.weathermate.home_screen.viewmodel.HomeViewModelFactory
import com.example.weathermate.network.ApiState
import com.example.weathermate.network.ConcreteRemoteSource
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.collectLatest
import java.util.*


class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    private val PERMISSION_ID = 10
    private val REQUEST_CODE_MY_DIALOG = 10

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

        factory =
            HomeViewModelFactory(
                HomeRepository.getInstance(
                    ConcreteRemoteSource(),
                    ConcreteLocalSource(requireContext())
                )
            )

        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        if (checkForInternet(requireContext())) {
            Log.i(TAG, "checkForInternet: yes")
            getLocation()
        } else {
            Log.i(TAG, "checkForInternet: no")
            //----------if its first time SharedPreferences-----------

            //get data from room
            homeViewModel.getLocalWeatherDetails()

            lifecycleScope.launchWhenStarted {
                homeViewModel.roomStateFlow.collect {
                    when(it){
                        is DbState.Success->{
                            _binding.weatherApiResponse = it.data

                            //Can be modified using data binding
                            Log.i(TAG, "Room: ${it.data.locationName}")
                            _binding.tvCurrentLocation.text = it.data.locationName.split(",").get(1)

                            _binding.todayImg.setImageResource(
                                photos.get(
                                    it.data.currentForecast!!.weather.get(
                                        0
                                    ).icon
                                )!!
                            )

                            hourlyAdapter.submitList(it.data.hourlyForecast.take(24))
                            _binding.recHourly.adapter = hourlyAdapter

                            dailyAdapter.submitList(it.data.dailyForecast.drop(1).take(7))
                            _binding.recNextDays.adapter = dailyAdapter

                            _binding.progressBar.visibility = View.GONE
                            _binding.mainGroup.visibility = View.VISIBLE
                        }
                        is DbState.Loading ->{
                            Log.i(TAG, "getWeatherDetails: loading")
                            _binding.progressBar.visibility = View.VISIBLE
                            _binding.mainGroup.visibility = View.GONE
                        }

                        is DbState.Failure -> {
                            Log.i(TAG, "onCreateView: Failed to get data from room ${it.msg.printStackTrace()}")
                        }
                    }
                }
            }
        }

        return _binding.root
    }


    private fun getWeatherDetails(
        latitude: Double,
        longitude: Double,
        units: String,
        lang: String
    ) {
        homeViewModel.getWeatherDetails(latitude, longitude, units, lang)
        //Start Consuming
        lifecycleScope.launchWhenResumed {
            homeViewModel.retrofitStateFlow.collectLatest {
                when (it) {
                    is ApiState.Success -> {
                        Log.i(
                            TAG,
                            "getWeatherDetails: ${it.data.locationName}${it.data.cityLatitude}${it.data.cityLongitude}"
                        )

                        _binding.weatherApiResponse = it.data

                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val address = geocoder.getFromLocation(
                            it.data.cityLatitude,
                            it.data.cityLongitude,
                            1
                        ) as List<Address>

                        //get the complete address
                        it.data.locationName = address.get(0).getAddressLine(0)

                        //ROOM
                        homeViewModel.insertWeatherDetails(it.data)

                        _binding.tvCurrentLocation.text = it.data.locationName.split(",").get(1)

                        _binding.todayImg.setImageResource(
                            photos.get(
                                it.data.currentForecast!!.weather.get(
                                    0
                                ).icon
                            )!!
                        )

                        hourlyAdapter.submitList(it.data.hourlyForecast.take(24))
                        _binding.recHourly.adapter = hourlyAdapter

                        dailyAdapter.submitList(it.data.dailyForecast.drop(1).take(7))
                        _binding.recNextDays.adapter = dailyAdapter

                        _binding.progressBar.visibility = View.GONE
                        _binding.mainGroup.visibility = View.VISIBLE
                    }
                    is ApiState.Loading -> {
                        Log.i(TAG, "getWeatherDetails: loading")
                        _binding.progressBar.visibility = View.VISIBLE
                        _binding.mainGroup.visibility = View.GONE
                    }
                    is ApiState.Failure -> {
                        //visiblity of whole layout gone and show error msg
                        Log.i(TAG, "getWeatherDetails: ${it.msg.printStackTrace()}")
                    }
                }
            }
        }
    }

    private fun isLocationEnable(): Boolean {
        //reserve reference of location manager
        //condition could be modified in any case i want
        Log.i(TAG, "isLocationEnable: ")
        val locationManager: LocationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun getLocation() {
        Log.i(TAG, "getLocation: ")
        if (checkPermissions()) {//if permissions available after granting them from the user
            Log.i(TAG, "checkPermissions: enabled")
            if (isLocationEnable()) {//if any location is available
                Log.i(TAG, "isLocationEnable: enabled")
                requestNewLocationData()
            } else {
                Log.i(TAG, "isLocationEnable: not enabled")
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        } else {
            Log.i(TAG, "checkPermissions: not enabled")
            requestPermissions()
        }
    }

    @Suppress("MissingPermission")
    private fun requestNewLocationData() {
        Log.i(TAG, "requestNewLocationData: ")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY, null
        ).addOnSuccessListener { location: Location? ->
            Log.i(TAG, "requestNewLocationData: success")
            if (location == null) {
                Toast.makeText(requireContext(), "Cannot get location.", Toast.LENGTH_SHORT)
                    .show()
                Log.i(TAG, "requestNewLocationData: null")
            } else {
                Log.i(TAG, "requestNewLocationData: ${location.longitude}")
                getWeatherDetails(location.latitude, location.longitude, "metric", "en")
            }
        }.addOnCanceledListener {
            Log.i(TAG, "requestNewLocationData: failed")
        }
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
            PERMISSION_ID
        )
        //at this function the prompt is displayed and to check on the action of it
        //through this callback fun onRequestPermissionsResult()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i(TAG, "onRequestPermissionsResult: worked")

        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, do something with the location
                getLocation()
            } else {
                // Permission denied, show an error message
                val dialogFragment = MyDialogFragment()
                dialogFragment.show(parentFragmentManager, "MyDialogFragment")
                dialogFragment.isCancelable = false
                dialogFragment.setTargetFragment(this, REQUEST_CODE_MY_DIALOG)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_MY_DIALOG && resultCode == Activity.RESULT_OK) {
            if (data != null) {//i don't need any data from the intent
                Log.i(TAG, "onActivityResult: testing")
                getLocation()
            }
        }
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
}