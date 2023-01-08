package com.example.app

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.app.recyclerView.StationsRecyclerViewAdapter
import com.example.app.viewModel.StationViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class StationActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_COARSE_LOCATION = 2

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var stationViewModel: StationViewModel

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.station_activity)

        // Get user location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // RecyclerView
        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.locationsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = StationsRecyclerViewAdapter(this)
        recyclerView.adapter = adapter

        // ViewModel
        stationViewModel = androidx.lifecycle.ViewModelProvider(this)[StationViewModel::class.java]
        stationViewModel.sortedStations.observe(this) { locations ->
            // Update the cached copy of the locations in the adapter.
            locations?.let { adapter.setLocations(it) }
        }

        // setup swipe refresh
        val swipeRefresh = findViewById<View>(R.id.swipeRefresh) as SwipeRefreshLayout
        swipeRefresh.setOnRefreshListener {
            refreshStations()
            swipeRefresh.isRefreshing = false
        }

        refreshStations()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_COARSE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sortStationsByDistance()
            } else {
                Toast.makeText(this, "Stations will be sorted and distance will be shown if the permission for location is enabled.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sortStationsByDistance() {
        stationViewModel.allStations.observe(this) { locations ->
            // Update the cached copy of the locations in the sortedLocations in the ViewModel.
            locations?.let {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestGPS()
                } else {
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

    private fun requestGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            sortStationsByDistance()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_COARSE_LOCATION)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_COARSE_LOCATION)
            }
        }
    }

    private fun refreshStations() {
        sortStationsByDistance()
    }
}
