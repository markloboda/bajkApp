package com.example.app.data.reservation

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.*

@Dao
interface ReservationDao {

    @Query("SELECT * FROM reservation_table WHERE bikeId = :bikeId ORDER BY zacetekRezervacije ASC")
    suspend fun getAllReservations(bikeId: Long): List<Reservation?>

    @Query("SELECT * FROM reservation_table WHERE :now BETWEEN zacetekRezervacije AND konecRezervacije")
    suspend fun getAllActiveReservations(now: Long): List<Reservation?>

    @Insert
    suspend fun insertReservation(reservation: Reservation)


}