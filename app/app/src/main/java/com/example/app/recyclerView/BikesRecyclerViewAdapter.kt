package com.example.app.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.data.bike.Bike

class BikesRecyclerViewAdapter :
    RecyclerView.Adapter<BikesRecyclerViewAdapter.ViewHolder>() {

    private var bikes: List<Bike> = listOf()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bikeId = view.findViewById<TextView>(R.id.bikeIdTextView)
        val bikeTitle = view.findViewById<TextView>(R.id.bikeTitleTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.bike_card, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentBike = bikes[position]
        holder.bikeId.text = currentBike.id.toString()
        holder.bikeTitle.text = currentBike.title
    }

    override fun getItemCount() = bikes.size

    fun setBikes(bikes: List<Bike>) {
        this.bikes = bikes
        // use position to update only the changed item
        notifyItemRangeChanged(0, bikes.size)
    }
}