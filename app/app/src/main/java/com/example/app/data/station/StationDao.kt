package com.example.app.data.station;

import android.location.Location
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
interface StationDao {
    @Query("SELECT * FROM location_table")
    fun readAllLocations(): LiveData<List<Station>>

}
