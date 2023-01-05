package com.example.app.data.station

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param

@Entity(tableName = "location_table")
data class Station(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @Ignore
    var distance: Float = 0f

    fun distanceTo(location: Location) : Float {
        val locationA = Location("point A")
        locationA.latitude = latitude
        locationA.longitude = longitude
        distance = locationA.distanceTo(location)
        return distance
    }
}