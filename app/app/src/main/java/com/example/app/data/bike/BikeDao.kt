package com.example.app.data.bike

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BikeDao {
    @Query("SELECT * FROM bike_table ORDER BY id ASC")
    fun readAllBikes(): LiveData<List<Bike>>

    @Query("SELECT * FROM bike_table WHERE id=:bikeId")
    suspend fun readBikeById(bikeId: Long): Bike?

    @Query("SELECT * FROM bike_table WHERE station_id = :stationId")
    suspend fun readBikesByStationId(stationId: Long): List<Bike?>

    @Query("SELECT * FROM bike_table WHERE station_id = :stationId AND spot_index = :spotIndex")
    suspend fun readBikeByStationIdAndSpotIndex(stationId: Long, spotIndex: Int): Bike?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBike(bike: Bike)

    @Update
    suspend fun updateBikes(vararg bikes: Bike)

    @Query("UPDATE bike_table SET station_id = :stationId, spot_index = :spotIndex WHERE id = :bikeId")
    suspend fun updateBike(bikeId: Long, stationId: Long, spotIndex: Int)
}