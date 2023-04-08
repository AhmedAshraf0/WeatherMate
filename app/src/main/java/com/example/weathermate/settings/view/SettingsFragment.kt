package com.example.weathermate.settings.view

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.weathermate.R
import com.example.weathermate.databinding.FragmentSettingsBinding
import java.util.*
import android.content.res.*
import android.os.Build
import android.text.TextUtils
import androidx.navigation.fragment.navArgs


class SettingsFragment : Fragment() {
    private val TAG = "SettingsFragment"
    private val PERMISSION_ID = 10
    private lateinit var _binding : FragmentSettingsBinding
    private lateinit var sharedPreferences : SharedPreferences
    private  lateinit var editor :SharedPreferences.Editor
    val args : SettingsFragmentArgs by navArgs()

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


        val testing = args.locationLatLng
        Log.i(TAG, "onViewCreated: ${testing}  -yay")

        //to be optimized
        if(sharedPreferences.getBoolean("is_gps",true)){
            Log.i(TAG, "onViewCreated: t")
            switchToGps()
        }else{
            Log.i(TAG, "onViewCreated: f")
            switchToMap()

            if(checkPermissions()){
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

        //---------To upgrade ui

        /*if(sharedPreferences.getString("lang","non").equals("en")){
            Log.i(TAG, "onViewCreated: en in lang")
            switchToEn()
        }else if(sharedPreferences.getString("lang","non").equals("ar")){
            switchToAr()
        }else{
            Log.i(TAG, "onViewCreated: e in lang")
        }*/
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
                switchToMap()

                val navController = Navigation.findNavController(requireActivity(),
                    R.id.nav_host_fragment_content_main)
                val action = SettingsFragmentDirections.actionNavSettingsToMapFragment(true)
                navController.navigate(action)
            }
            _binding.rbGps.id -> {
                Log.i(TAG, "onRbClicked: rbGps")
                switchToGps()
            }

            _binding.rbCelsius.id -> {
                Log.i(TAG, "onRbClicked: rbCelsius")

                switchToCelsius()
            }
            _binding.rbFahrenheit.id -> {
                Log.i(TAG, "onRbClicked: rbFahrenheit")

                switchToFahrenheit()
            }
            _binding.rbKelvin.id -> {
                Log.i(TAG, "onRbClicked: rbKelvin")

                switchToKelvin()
            }

            _binding.rbEn.id -> {
                Log.i(TAG, "onRbClicked: rbEn")
                if(sharedPreferences.getString("lang","en").equals("ar")){
                    Log.i(TAG, "onRbClicked: in if")

                    switchToEn()
                    /*val navController = Navigation.findNavController(requireActivity(),
                        R.id.nav_host_fragment_content_main)
                    navController.navigate(R.id.action_nav_settings_self)*/
                }
            }
            _binding.rbAr.id -> {
                Log.i(TAG, "onRbClicked: rbAr")
                Log.i(TAG, "onRbClicked: ${sharedPreferences.getString("lang","z")}")
                if(sharedPreferences.getString("lang","en").equals("en")){
                    Log.i(TAG, "onRbClicked: in if")

                    switchToAr()
                    /*val navController = Navigation.findNavController(requireActivity(),
                        R.id.nav_host_fragment_content_main)
                    navController.navigate(R.id.action_nav_settings_self)*/
                }else{
                    print(sharedPreferences.getString("lang","z"))
                }
                Log.i(TAG, "onRbClicked: ${sharedPreferences.getString("lang","z")}")
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
    private fun setLocal(lang : String){
        val local = Locale(lang)
        Locale.setDefault(local)
        val config = Configuration()
        config.setLocale(local)
        requireActivity().baseContext.resources.updateConfiguration(config,requireActivity().baseContext.resources.displayMetrics)

        // Determine layout direction based on selected language
        val layoutDirection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (TextUtils.getLayoutDirectionFromLocale(local) == View.LAYOUT_DIRECTION_RTL) {
                View.LAYOUT_DIRECTION_RTL
            } else {
                View.LAYOUT_DIRECTION_LTR
            }
        } else {
            View.LAYOUT_DIRECTION_LTR
        }

        // Set layout direction on the root view
        requireActivity().window.decorView.layoutDirection = layoutDirection

        Log.i(TAG, "setLocal: hi")
        this.requireActivity().recreate()
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