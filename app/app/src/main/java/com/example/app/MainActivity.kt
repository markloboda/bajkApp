package com.example.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var bikeViewModel: BikeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.bikesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = BikesRecyclerViewAdapter()
        recyclerView.adapter = adapter

        // ViewModel
        bikeViewModel = ViewModelProvider(this).get(BikeViewModel::class.java)
        bikeViewModel.allBikes.observe(this) { bikes ->
            adapter.setBikes(bikes)
        }
    }
}