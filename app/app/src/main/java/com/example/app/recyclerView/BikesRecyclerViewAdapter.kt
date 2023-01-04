package com.example.app.recyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.app.BikeListActivity
import com.example.app.MainActivity
import com.example.app.R
import com.example.app.data.bike.Bike

class BikesRecyclerViewAdapter(private val context: Context) :
    RecyclerView.Adapter<BikesRecyclerViewAdapter.ViewHolder>() {

    private var bikes: List<Bike> = listOf()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bikeId = view.findViewById<TextView>(R.id.bikeIdTextView)
        val bikeTitle = view.findViewById<TextView>(R.id.bikeTitleTextView)
        val bikeStatus = view.findViewById<TextView>(R.id.bikeStatusTextView)
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
        holder.bikeStatus.text = if (currentBike.status) "Na voljo" else "Izposojen"
        holder.bikeStatus.setTextColor(
            if (currentBike.status) ContextCompat.getColor(
                holder.bikeStatus.context,
                R.color.status_red
            )
            else ContextCompat.getColor(holder.bikeStatus.context, R.color.status_green)
        )
        holder.itemView.setOnClickListener {
            (context as BikeListActivity).reservationDataDialog(
                currentBike.id,
                currentBike.title,
                currentBike.status
            )
        }

        holder.itemView.setOnLongClickListener {
            (context as BikeListActivity).bikeDataDialog(currentBike.id)
            true
        }
    }

    override fun getItemCount() = bikes.size

    fun setBikes(bikes: List<Bike>) {
        this.bikes = bikes
        // use position to update only the changed item
        notifyItemRangeChanged(0, bikes.size)
    }
}