package com.example.app.data.location

import androidx.lifecycle.LiveData

class LocationRepository(private val locationDao: LocationDao) {
    val readAllLocations: LiveData<List<Location>> = locationDao.readAllLocations()
}