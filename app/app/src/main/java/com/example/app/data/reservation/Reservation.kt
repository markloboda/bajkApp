package com.example.app.data.reservation

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservation_table")
data class Reservation(
    val userId: Long,
    val bikeId: Long,
    val startLocationId: Long,
    val endLocationId: Long,
    val startTime: Long,
    val endTime: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}