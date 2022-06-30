package com.example.app.data.bike

import androidx.lifecycle.LiveData

class BikeRepository(private val bikeDao: BikeDao) {
    val readAllBikes: LiveData<List<Bike>> = bikeDao.readAllBikes()

    fun insertAllBikes(vararg bikes: Bike) = bikeDao.insertAllBikes(*bikes)
}