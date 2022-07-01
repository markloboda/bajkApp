package com.example.app.data.bike

import androidx.lifecycle.LiveData

class BikeRepository(private val bikeDao: BikeDao) {
    val readAllBikes: LiveData<List<Bike>> = bikeDao.readAllBikes()

    suspend fun readBikeById(bikeId: Long) = bikeDao.readBikeById(bikeId)

    suspend fun updateBike(bike: Bike) = bikeDao.updateBike(bike)

    suspend fun updateBikes(vararg bikes: Bike) = bikeDao.updateBikes(*bikes)

    suspend fun resetAllBikesAvailability() = bikeDao.resetAllBikesAvailability(true)

    suspend fun setBikeAvailabilityFalse(bikeIds: List<Long>) = bikeDao.setBikeAvailabilityFalse(false, bikeIds)
}