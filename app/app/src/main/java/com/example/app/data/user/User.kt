package com.example.app.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    val ime: String,
    val priimek: String,
    val sektor: String
    ) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var razdaljaKm : Int = 0
}