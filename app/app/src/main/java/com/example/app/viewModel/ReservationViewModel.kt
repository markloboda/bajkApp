package com.example.app.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.app.data.AppDatabase
import com.example.app.data.reservation.Reservation
import com.example.app.data.reservation.ReservationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReservationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ReservationRepository

    var reservationLive: MutableLiveData<List<Reservation?>> = MutableLiveData()

    init {
        val reservationDao = AppDatabase.getDatabase(application).reservationDao()
        repository = ReservationRepository(reservationDao)
    }

    fun getAllBikeReservations(bikeId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            reservationLive.postValue(repository.getAllBikeReservations(bikeId))

        }
    }

    fun insertReservation(reservation: Reservation) {
        viewModelScope.launch(Dispatchers.IO) { repository.insertReservation(reservation) }
    }

}
