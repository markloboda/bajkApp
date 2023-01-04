package com.example.app.data.station

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_table")
data class Station(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    fun distanceTo(location: Location) : Float {
        val locationA = Location("point A")
        locationA.latitude = latitude
        locationA.longitude = longitude
        return locationA.distanceTo(location)
    }
}
