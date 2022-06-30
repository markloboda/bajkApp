package com.example.app.data.reservation

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservation_table")
data class Reservation(
    val userId: Long,
    val bikeId: Long,
    val zacetekRezervacije: String,
    val konecRezervacije: String,
    val prevozeniKm: Int,
    val namen: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}