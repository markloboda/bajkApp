package com.example.app.data.location

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_table")
data class Location(
    @ColumnInfo(name = "title") val title: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}