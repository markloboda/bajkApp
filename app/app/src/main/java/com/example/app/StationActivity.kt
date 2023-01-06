package com.example.app

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.recyclerView.LocationsRecyclerViewAdapter
import com.example.app.viewModel.StationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class StationActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var stationViewModel: StationViewModel

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_activity)

        // Get user location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)


        // RecyclerView
        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.locationsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = LocationsRecyclerViewAdapter(this)
        recyclerView.adapter = adapter

        // ViewModel
        stationViewModel = androidx.lifecycle.ViewModelProvider(this)[StationViewModel::class.java]
        stationViewModel.sortedStations.observe(this) { locations ->
            // Update the cached copy of the locations in the adapter.
            locations?.let { adapter.setLocations(it) }
        }

        stationViewModel.allStations.observe(this) { locations ->
            // Update the cached copy of the locations in the sortedLocations in the ViewModel.
            locations?.let {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)
                }

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && isGPSEnabled()) {
                    fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            // Sort locations in by distance from user in recyclerView
                            stationViewModel.sortLocationsByDistance(location)
                        }
                    }
                }
            }
        }
    }

    private fun isGPSEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}
