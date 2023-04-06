package com.example.weathermate.map

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weathermate.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(){
    private val TAG = "MapFragment"
    private lateinit var supportMapFragment: SupportMapFragment
    private lateinit var client: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inflate the layout for this fragment
        client = LocationServices.getFusedLocationProviderClient(requireActivity())
        supportMapFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.google_maps) as SupportMapFragment

        if(supportMapFragment == null){
            Log.i(TAG, "onCreateView: null")
            supportMapFragment = SupportMapFragment.newInstance()
            requireFragmentManager().beginTransaction().replace(R.id.google_maps,supportMapFragment).commit()
        }
        getCurrentLocation()
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
                    it.addMarker(markerOptions)

                }
            }
        }.addOnCanceledListener {
            Log.i(TAG, "getCurrentLocation: failed")
        }
    }
}