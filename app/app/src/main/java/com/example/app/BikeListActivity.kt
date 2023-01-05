package com.example.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.app.recyclerView.BikesRecyclerViewAdapter
import com.example.app.viewModel.BikeViewModel
import com.example.app.viewModel.StationViewModel
import java.text.SimpleDateFormat
import java.util.*

class BikeListActivity : AppCompatActivity() {

    private lateinit var bikeViewModel: BikeViewModel
    private lateinit var locationViewModel: StationViewModel

    private var stationId: Long = -1
    private lateinit var stationTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bike_list_activity)

        val titleText = findViewById<TextView>(R.id.stationTextView)

        // Get the station id and title from the intent
        stationId = intent.getLongExtra("stationId", -1)
        stationTitle = intent.getStringExtra("stationTitle") ?: "Unknown"

        // Set the title
        titleText.text = stationTitle

        // RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.bikesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = BikesRecyclerViewAdapter(this)
        recyclerView.adapter = adapter

        // ViewModel
        bikeViewModel = ViewModelProvider(this)[BikeViewModel::class.java]
        bikeViewModel.bikesByStationId.observe(this) { bikes ->
            adapter.setBikes(bikes)
        }
        refreshBikes()

        // setup swipe refresh
        val swipeRefresh = findViewById<View>(R.id.swipeRefresh) as SwipeRefreshLayout
        swipeRefresh.setOnRefreshListener {
            refreshBikes()
            swipeRefresh.isRefreshing = false
        }
    }

    private fun refreshBikes() {
        // check bike availability
        bikeViewModel.readBikesByStationId(stationId)
    }
}
