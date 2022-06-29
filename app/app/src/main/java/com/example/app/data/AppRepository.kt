package com.example.app.data

import androidx.lifecycle.LiveData

class AppRepository(private val bikeDao: BikeDao) {
    val readAllBikes: LiveData<List<Bike>> = bikeDao.readAllBikes()

    fun insertAllBikes(vararg bikes: Bike) {
        bikeDao.insertAllBikes(*bikes)
    }
}