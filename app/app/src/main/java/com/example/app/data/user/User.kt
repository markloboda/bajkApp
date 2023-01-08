package com.example.app.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @ColumnInfo(name="phone") val phone: String,
    @ColumnInfo(name="name") val name: String,
    @ColumnInfo(name="surname") val surname: String,
    @ColumnInfo(name="bike_id") val bikeId: Long = -1
    ) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}