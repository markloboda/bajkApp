package com.example.app.data.bike

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BikeDao {
    @Query("SELECT * FROM bike_table ORDER BY id ASC")
    fun readAllBikes(): LiveData<List<Bike>>

    @Query("SELECT * FROM bike_table WHERE id=:bikeId")
    suspend fun readBikeById(bikeId: Long): Bike?

    @Query("UPDATE bike_table SET status=:trueBool")
    suspend fun resetAllBikesAvailability(trueBool: Boolean)

    @Query("UPDATE bike_table SET status=:falseBool WHERE id IN (:bikeIds)")
    suspend fun setBikeAvailabilityFalse(falseBool: Boolean, bikeIds: List<Long>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateBike(bike: Bike)

    @Update
    suspend fun updateBikes(vararg bikes: Bike)
}