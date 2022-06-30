package com.example.app.data.reservation

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface ReservationDao {

    @Insert
    suspend fun insertReservation(reservation: Reservation)


}