package com.example.app.data.reservation

import com.example.app.data.bike.Bike

class ReservationRepository(private val reservationDao: ReservationDao) {

    suspend fun getAllBikeReservations(bikeId: Long) : List<Reservation?> = reservationDao.getAllReservations(bikeId)

    suspend fun insertReservation(reservation: Reservation) = reservationDao.insertReservation(reservation)

}