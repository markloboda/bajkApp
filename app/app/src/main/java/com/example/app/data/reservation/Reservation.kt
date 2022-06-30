package com.example.app.data.reservation

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservation_table")
data class Reservation(
    val userId: Int,
    val bikeId: Int,
    val prevozeniKm: Int,
    val namen: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}