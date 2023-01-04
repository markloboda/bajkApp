package com.example.app.data.station

import android.location.Location
import androidx.lifecycle.LiveData

class StationRepository(private val stationDao: StationDao) {
    val readAllLocations: LiveData<List<Station>> = stationDao.readAllLocations()
}