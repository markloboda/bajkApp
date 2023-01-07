package com.example.app.data.bike

import androidx.lifecycle.LiveData

class BikeRepository(private val bikeDao: BikeDao) {
    val readAllBikes: LiveData<List<Bike>> = bikeDao.readAllBikes()

    suspend fun readBikeById(bikeId: Long) = bikeDao.readBikeById(bikeId)

    suspend fun readBikesByStationId(stationId: Long): List<Bike?> = bikeDao.readBikesByStationId(stationId)

    suspend fun readBikeByStationIdAndSpotIndex(stationId: Long, spotIndex: Int): Bike? = bikeDao.readBikeByStationIdAndSpotIndex(stationId, spotIndex)

    suspend fun updateBike(bike: Bike) = bikeDao.updateBike(bike)

    suspend fun updateBikes(vararg bikes: Bike) = bikeDao.updateBikes(*bikes)

    suspend fun updateBike(bikeId: Long, stationId: Long, spotIndex: Int) = bikeDao.updateBike(bikeId, stationId, spotIndex)
}