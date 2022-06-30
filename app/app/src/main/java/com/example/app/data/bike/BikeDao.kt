package com.example.app.data.bike

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BikeDao {
    @Query("SELECT * FROM bike_table ORDER BY id ASC")
    fun readAllBikes(): LiveData<List<Bike>>

    @Query("SELECT * FROM bike_table WHERE id=:bikeId")
    suspend fun readBikeById(bikeId: Long): Bike?

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateBike(bike: Bike)
}