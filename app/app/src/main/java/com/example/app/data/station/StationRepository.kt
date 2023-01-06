package com.example.app.data.station

import androidx.lifecycle.LiveData

class StationRepository(private val stationDao: StationDao) {
    val readAllLocations: LiveData<List<Station>> = stationDao.readAllLocations()

    suspend fun readStationById(stationId: Long) = stationDao.readStationById(stationId)

    suspend fun updateStation(station: Station) = stationDao.updateStation(station)
}