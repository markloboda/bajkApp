package com.example.app.data.reservation

import java.util.*

class ReservationRepository(private val reservationDao: ReservationDao) {

    suspend fun getAllBikeReservations(bikeId: Long) : List<Reservation?> = reservationDao.getAllReservations(bikeId)

    suspend fun getAllActiveReservations() : List<Reservation?> = reservationDao.getAllActiveReservations(Calendar.getInstance().timeInMillis)

    suspend fun insertReservation(reservation: Reservation) = reservationDao.insertReservation(reservation)

}