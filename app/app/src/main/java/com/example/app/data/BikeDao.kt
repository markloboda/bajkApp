package com.example.app.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BikeDao {
    @Query("SELECT * FROM bike_table ORDER BY id ASC")
    fun readAllBikes(): LiveData<List<Bike>>

    @Query("SELECT COUNT(id) FROM bike_table")
    suspend fun count(): Int

    @Query("SELECT * FROM bike_table WHERE id=:id")
    fun readBikeById(id: Int): LiveData<Bike>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllBikes(vararg bikes: Bike)
}