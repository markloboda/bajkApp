package com.example.app

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.app.recyclerView.BikesRecyclerViewAdapter
import com.example.app.viewModel.BikeViewModel

class BikeListActivity : AppCompatActivity() {

    private lateinit var bikeViewModel: BikeViewModel

    private var stationId: Long = -1
    private lateinit var stationTitle: String

    private var amountOfBikes: MutableLiveData<Int> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bike_list_activity)

        val stationAddress = findViewById<TextView>(R.id.stationAddressTextView)
        val titleText = findViewById<TextView>(R.id.stationTextView)
        val amountOfBikesText = findViewById<TextView>(R.id.occupiedTextView)
        val freeText = findViewById<TextView>(R.id.freeTextView)

        // Get the station id and title from the intent
        stationId = intent.getLongExtra("stationId", -1)
        stationTitle = intent.getStringExtra("stationTitle") ?: "Unknown"

        // set address
        stationAddress.text = intent.getStringExtra("address") ?: "Unknown"
        // Set the title
        titleText.text = stationTitle

        // set amount of bikes
        amountOfBikes.observe(this) {
            amountOfBikesText.text = "Amount of bikes:\n${ it }"
        }
        freeText.text = "Number of spots:\n${intent.getIntExtra("free", -1)}"



        // RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.bikesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = BikesRecyclerViewAdapter()
        recyclerView.adapter = adapter

        // ViewModel
        bikeViewModel = ViewModelProvider(this)[BikeViewModel::class.java]
        bikeViewModel.bikesByStationId.observe(this) { bikes ->
            bikes.let {
                adapter.setBikes(bikes.filterNotNull())
                amountOfBikes.postValue(bikes.size)
            }
        }
        bikeViewModel.readBikesByStationId(stationId)

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
