package com.example.weathermate

import android.os.Bundle
import android.view.Menu
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.weathermate.databinding.ActivityMainBinding
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("Home", "onCreate: ")

        //should be before binding
        val sharedPreferences = getSharedPreferences(this)
        val editor = sharedPreferences.edit()

        //if 0 -> yes first and no shared saved so that's default value
        //if 1 -> yes user entered once before there is shared saved
        //if-1 -> no it's not first time

        if (sharedPreferences.getInt("first_time", 0) == 0) {
            Log.i("Home", "onCreate: 1")
            editor.putInt("first_time", 1)
            editor.putBoolean("is_gps", true)
            editor.putString("units", "metric")//m/s | celsius
            editor.putString("lang", "en")
            editor.apply()
        } else if (sharedPreferences.getInt("first_time", 0) == 1 &&
            sharedPreferences.getBoolean("succeed_once", false)
        ) {
            Log.i("Home", "onCreate: -1")
            editor.putInt("first_time", -1)
            editor.apply()
        }

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_alerts, R.id.nav_favs, R.id.nav_settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
    }

}