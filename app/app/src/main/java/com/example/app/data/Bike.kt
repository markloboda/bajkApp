package com.example.app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bike_table")
data class Bike(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "status") val status: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}