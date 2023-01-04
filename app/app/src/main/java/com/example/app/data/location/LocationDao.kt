package com.example.app.data.location;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
interface LocationDao {
    @Query("SELECT * FROM location_table")
    fun readAllLocations(): LiveData<List<Location>>
}