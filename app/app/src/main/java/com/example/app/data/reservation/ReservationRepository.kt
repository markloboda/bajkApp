package com.example.app.data.reservation

class ReservationRepository(private val reservationDao: ReservationDao) {

    suspend fun insertReservation(reservation: Reservation) = reservationDao.insertReservation(reservation)

}