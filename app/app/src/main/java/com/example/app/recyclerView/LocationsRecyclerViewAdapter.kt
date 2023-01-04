package com.example.app.recyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.data.location.Location

class LocationsRecyclerViewAdapter(private val context: Context) :
    RecyclerView.Adapter<LocationsRecyclerViewAdapter.ViewHolder>() {

    private var locations: List<Location> = listOf()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val locationId = view.findViewById<TextView>(R.id.locationIdTextView)
        val locationTitle = view.findViewById<TextView>(R.id.locationTitleTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.location_card, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentLocation = locations[position]
        holder.locationId.text = currentLocation.id.toString()
        holder.locationTitle.text = currentLocation.title
    }

    override fun getItemCount() = locations.size


}