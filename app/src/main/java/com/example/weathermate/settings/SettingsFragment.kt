package com.example.weathermate.settings

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.media.VolumeShaper.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.weathermate.R
import com.example.weathermate.databinding.FragmentSettingsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*
import android.content.res.*


class SettingsFragment : Fragment() {
    private val TAG = "SettingsFragment"
    private val PERMISSION_ID = 10
    private lateinit var _binding : FragmentSettingsBinding
    private lateinit var sharedPreferences : SharedPreferences
    private  lateinit var editor :SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        _binding.myFragment = this

        sharedPreferences = getSharedPreferences(requireActivity())
        editor = sharedPreferences.edit()
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated: ")

        //to be optimized
        if(sharedPreferences.getBoolean("is_gps",true)){
            Log.i(TAG, "onViewCreated: t")
            switchToGps()
        }else{
            Log.i(TAG, "onViewCreated: f")
            switchToMap()
            val navController = Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment_content_main)


// Navigate to the destination Fragment using the NavGraph and passing any arguments
            navController.navigate(R.id.action_nav_settings_to_mapFragment)
            if(checkPermissions()){
                //navigate to map fragment
                // Get the NavController object from the Fragment's view


            }else{
                requestPermissions()
            }
        }

        when(sharedPreferences.getString("units","metric")){
            "metric" ->{
                switchToCelsius()
            }
            "imperial" ->{
                switchToFahrenheit()
            }
            "standard" ->{
                switchToKelvin()
            }
            else ->{
                Log.i(TAG, "onViewCreated: Error in initialize views")
            }
        }

        if(sharedPreferences.getString("lang","non").equals("en")){
            Log.i(TAG, "onViewCreated: en in lang")
            switchToEn()
        }else if(sharedPreferences.getString("lang","non").equals("ar")){
            switchToAr()
        }else{
            Log.i(TAG, "onViewCreated: e in lang")
        }
    }
    fun onRbClicked(view: View){
        //--------units-----
        //metric -> C & m/s
        //imperial -> F & mph
        //standard -> K & m/s

        //-----lang---

        //-----is_gps---
        //gps -> true|| map
        when(view.id){
            _binding.rbMap.id -> {
                Log.i(TAG, "onRbClicked: rbMap")
                editor.putBoolean("is_gps",false)

                switchToMap()
            }
            _binding.rbGps.id -> {
                Log.i(TAG, "onRbClicked: rbGps")
                editor.putBoolean("is_gps",true)

                switchToGps()
            }

            _binding.rbCelsius.id -> {
                Log.i(TAG, "onRbClicked: rbCelsius")
                editor.putString("units","metric")

                switchToCelsius()
            }
            _binding.rbFahrenheit.id -> {
                Log.i(TAG, "onRbClicked: rbFahrenheit")
                editor.putString("units","imperial")

                switchToFahrenheit()
            }
            _binding.rbKelvin.id -> {
                Log.i(TAG, "onRbClicked: rbKelvin")
                editor.putString("units","standard")

                switchToKelvin()
            }

            _binding.rbEn.id -> {
                Log.i(TAG, "onRbClicked: rbEn")
                editor.putString("lang","en")

                switchToEn()
            }
            _binding.rbAr.id -> {
                Log.i(TAG, "onRbClicked: rbAr")
                editor.putString("lang","ar")

                switchToAr()
            }
        }
    }
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
    }

    private fun switchToGps(){
        _binding.rbGps.isSelected = true
        _binding.rbGps.setTextColor(ContextCompat.getColor(requireContext(),R.color.main_color))
        _binding.rbMap.isSelected = false
        _binding.rbMap.setTextColor(ContextCompat.getColor(requireContext(),R.color.degree_type))
        editor.putBoolean("is_gps",true)
        editor.apply()
    }
    private fun switchToMap(){
        _binding.rbGps.isSelected = false
        _binding.rbGps.setTextColor(ContextCompat.getColor(requireContext(),R.color.degree_type))
        _binding.rbMap.isSelected = true
        _binding.rbMap.setTextColor(ContextCompat.getColor(requireContext(),R.color.main_color))
        editor.putBoolean("is_gps",false)
        editor.apply()
    }
    private fun switchToCelsius(){
        //unchecked rbFahrenheit & rbKelvin
        _binding.rbFahrenheit.isSelected = false
        _binding.rbFahrenheit.setTextColor(ContextCompat.getColor(requireContext(),R.color.degree_type))
        _binding.rbKelvin.isSelected = false
        _binding.rbKelvin.setTextColor(ContextCompat.getColor(requireContext(),R.color.degree_type))


        //checked rbCelsius
        _binding.rbCelsius.isSelected = true
        _binding.rbCelsius.setTextColor(ContextCompat.getColor(requireContext(),R.color.main_color))
        editor.putString("units","metric")
        editor.apply()
    }
    private fun switchToFahrenheit(){
        _binding.rbCelsius.isSelected = false
        _binding.rbCelsius.setTextColor(ContextCompat.getColor(requireContext(),R.color.degree_type))
        _binding.rbKelvin.isSelected = false
        _binding.rbKelvin.setTextColor(ContextCompat.getColor(requireContext(),R.color.degree_type))

        _binding.rbFahrenheit.isSelected = true
        _binding.rbFahrenheit.setTextColor(ContextCompat.getColor(requireContext(),R.color.main_color))
        editor.putString("units","imperial")
        editor.apply()
    }
    private fun switchToKelvin(){
        _binding.rbCelsius.isSelected = false
        _binding.rbCelsius.setTextColor(ContextCompat.getColor(requireContext(),R.color.degree_type))
        _binding.rbFahrenheit.isSelected = false
        _binding.rbFahrenheit.setTextColor(ContextCompat.getColor(requireContext(),R.color.degree_type))

        _binding.rbKelvin.isSelected = true
        _binding.rbKelvin.setTextColor(ContextCompat.getColor(requireContext(),R.color.main_color))
        editor.putString("units","standard")
        editor.apply()
    }
    private fun switchToEn(){
        _binding.rbAr.isSelected = false
        _binding.rbAr.setTextColor(ContextCompat.getColor(requireContext(),R.color.degree_type))

        setLocal("en")

        _binding.rbEn.isSelected = true
        _binding.rbEn.setTextColor(ContextCompat.getColor(requireContext(),R.color.main_color))
        editor.putString("lang","en")
        editor.apply()
    }
    private fun switchToAr() {
        _binding.rbEn.isSelected = false
        _binding.rbEn.setTextColor(ContextCompat.getColor(requireContext(),R.color.degree_type))

        setLocal("ar")

        _binding.rbAr.isSelected = true
        _binding.rbAr.setTextColor(ContextCompat.getColor(requireContext(),R.color.main_color))
        editor.putString("lang","ar")
        editor.apply()
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
            PERMISSION_ID
        )
        //at this function the prompt is displayed and to check on the action of it
        //through this callback fun onRequestPermissionsResult()
    }

    private fun setLocal(lang : String){
        val local = Locale(lang)
        Locale.setDefault(local)
        val config = Configuration()
        config.setLocale(local)
        requireActivity().baseContext.resources.updateConfiguration(config,requireActivity().baseContext.resources.displayMetrics)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }
        }
    }
}