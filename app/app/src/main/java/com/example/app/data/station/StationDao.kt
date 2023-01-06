package com.example.app.data.station;

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update

@Dao
interface StationDao {
    @Query("SELECT * FROM location_table")
    fun readAllLocations(): LiveData<List<Station>>

    @Query("SELECT * FROM location_table WHERE id = :stationId")
    suspend fun readStationById(stationId: Long): Station

    @Update(entity = Station::class)
    suspend fun updateStation(station: Station)
}
