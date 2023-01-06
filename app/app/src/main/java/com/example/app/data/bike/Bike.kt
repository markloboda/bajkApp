package com.example.app.data.bike

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bike_table")
data class Bike(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "station_id") val locationId: Long,
    @ColumnInfo(name = "spot_id") val spotId: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}