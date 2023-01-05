package com.example.app.recyclerView

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app.BikeListActivity
import com.example.app.R
import com.example.app.data.station.Station

class LocationsRecyclerViewAdapter(private val context: Context) :
    RecyclerView.Adapter<LocationsRecyclerViewAdapter.ViewHolder>() {

    private var stations: List<Station> = listOf()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val locationId = view.findViewById<TextView>(R.id.locationIdTextView)
        val locationTitle = view.findViewById<TextView>(R.id.locationTitleTextView)
        val locationDistance = view.findViewById<TextView>(R.id.locationDistanceTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.location_card, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Setup the data for the card
        val currentLocation = stations[position]
        holder.locationId.text = currentLocation.id.toString()
        holder.locationTitle.text = currentLocation.title
        val meters = currentLocation.distance.toInt()
        val kilometers = meters / 1000
        holder.locationDistance.text = if (kilometers > 0) {
            "${kilometers}km"
        } else {
            "${meters}m"
        }

        // Setup click listener
        holder.itemView.setOnClickListener {
            val intent = Intent(context, BikeListActivity::class.java)
            intent.putExtra("stationId", currentLocation.id)
            intent.putExtra("stationTitle", currentLocation.title)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = stations.size

    fun setLocations(stations: List<Station>) {
        this.stations = stations
        notifyItemRangeChanged(0, stations.size)
    }
}