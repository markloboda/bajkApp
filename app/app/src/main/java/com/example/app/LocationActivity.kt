package com.example.app

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app.recyclerView.LocationsRecyclerViewAdapter
import com.example.app.viewModel.LocationViewModel

class LocationActivity : AppCompatActivity() {

    private lateinit var locationViewModel: LocationViewModel

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.location_activity)

        // RecyclerView
        val recyclerView =
            findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.locationsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = LocationsRecyclerViewAdapter(this)
        recyclerView.adapter = adapter

        // ViewModel
        locationViewModel =
            androidx.lifecycle.ViewModelProvider(this).get(LocationViewModel::class.java)
        locationViewModel.allLocations.observe(this, androidx.lifecycle.Observer { locations ->
            // Update the cached copy of the locations in the adapter.
            locations?.let { adapter.setLocations(it) }
        })
    }
}
