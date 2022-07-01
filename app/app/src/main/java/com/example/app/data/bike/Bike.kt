package com.example.app.data.bike

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bike_table")
data class Bike(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "drivenKm") var drivenKm: Int,
    @ColumnInfo(name = "status") var status: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}