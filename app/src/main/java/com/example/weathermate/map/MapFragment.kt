package com.example.weathermate.map

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.weathermate.R
import com.example.weathermate.settings.SettingsFragmentArgs
import com.example.weathermate.settings.SettingsFragmentDirections
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class MapFragment : Fragment() , OnMapReadyCallback{
    private val TAG = "MapFragment"
    private lateinit var supportMapFragment: SupportMapFragment
    private lateinit var client: FusedLocationProviderClient
    private lateinit var sharedPreferences : SharedPreferences
    private  lateinit var editor :SharedPreferences.Editor
    private lateinit var searchTextField: SearchView
    private lateinit var map : GoogleMap
    private lateinit var marker : Marker
    val args : MapFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = getSharedPreferences(requireActivity())
        editor = sharedPreferences.edit()
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inflate the layout for this fragment
        searchTextField = view.findViewById(R.id.tf)
        searchTextField.setOnQueryTextListener(object: OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchResult = searchTextField.query.toString()
                var addressList : List<Address>? = null

                if(!searchResult.isNullOrEmpty()){
                    val geocoder = Geocoder(requireActivity())
                    try{
                        addressList = geocoder.getFromLocationName(searchResult, 1)
                    }catch (e:IOException){
                        Log.i(TAG, "onQueryTextSubmit: ${e.printStackTrace()}")
                    }
                    val latLng = LatLng(addressList!!.get(0).latitude,addressList.get(0).longitude)
                    marker.remove()
                    map.addMarker(MarkerOptions().position(latLng))!!.title = searchResult
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10f))
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        client = LocationServices.getFusedLocationProviderClient(requireActivity())
        supportMapFragment = childFragmentManager.findFragmentById(R.id.google_maps) as SupportMapFragment
        getCurrentLocation()


        supportMapFragment.getMapAsync(this)
    }

    @Suppress("MissingPermission")
    private fun getCurrentLocation() {
        Log.i(TAG, "requestNewLocationData: ")
        client.lastLocation.addOnSuccessListener { location: Location? ->
            Log.i(TAG, "getCurrentLocation: success")
            if (location != null) {
                supportMapFragment.getMapAsync {
                    //initialize lat and long
                    val latlng = LatLng(location.latitude,location.longitude)

                    //initialize marker options
                    val markerOptions = MarkerOptions().position(latlng).title("i'm here")

                    //zoom map
                    it.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,10f))

                    //add marker on map
                    marker = it.addMarker(markerOptions)!!

                    it.setOnMapClickListener{latLng->
                        marker.remove()
                        marker = it.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                .draggable(false)
                                .title("Confirm location")

                            //title to be added
                        )!!
                    }

                    it.setOnMarkerClickListener {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Confirm location")
                            .setMessage("Are you sure you want to choose this location?")
                            .setPositiveButton("Yes") { dialog, which ->
                                // Return to the previous fragment and pass the selected location
                                val location = marker.position
                                Log.i(
                                    TAG,
                                    "getCurrentLocation: ${location.longitude} ${location.latitude}"
                                )
                                editor.putString("location","${location.longitude},${location.latitude}")
                                editor.apply()


                                //--until adding a button to take this actions
                                if(args.isFromSettings){
                                    val navController = Navigation.findNavController(requireActivity(),
                                        R.id.nav_host_fragment_content_main)
                                    val action = MapFragmentDirections.actionMapFragmentToNavSettings(
                                        "${location.longitude},${location.latitude}")
                                    navController.navigate(action)
                                }else{
                                    //to nav to favs screen
                                }
                            }
                            .setNegativeButton("No", null)
                            .show()

                        false
                    }
                }
            }
        }.addOnCanceledListener {
            Log.i(TAG, "getCurrentLocation: failed")
        }
    }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
    }
}