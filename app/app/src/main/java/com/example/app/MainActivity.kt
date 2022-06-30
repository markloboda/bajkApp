package com.example.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.recyclerView.BikesRecyclerViewAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var dialogBuilder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog

    private lateinit var bikeViewModel: BikeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.bikesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = BikesRecyclerViewAdapter(this)
        recyclerView.adapter = adapter

        // ViewModel
        bikeViewModel = ViewModelProvider(this).get(BikeViewModel::class.java)
        bikeViewModel.allBikes.observe(this) { bikes ->
            adapter.setBikes(bikes)
        }
    }

    fun dataDialog(bikeId: Int, bikeTitle: String, bikeStatus: Boolean) {
        if (!bikeStatus) return
        dialogBuilder = AlertDialog.Builder(this, R.style.Theme_App)
        dialogBuilder.setTitle("Izposoja kolesa $bikeTitle")
        dialogBuilder.setIcon(R.drawable.logo)
        val dataPopupView: View = layoutInflater.inflate(R.layout.reservation_dialog, null)
        dialogBuilder.setView(dataPopupView)
        dialog = dialogBuilder.create()
        dialog.show()
        // setup dialog data
    }
}