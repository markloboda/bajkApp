package com.example.app.viewModel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.app.data.AppDatabase
import com.example.app.data.station.Station
import com.example.app.data.station.StationRepository

class StationViewModel(application: Application) : AndroidViewModel(application) {

    val allLocations: LiveData<List<Station>>
    val repository: StationRepository
    val sortedLocations: MutableLiveData<List<Station>> = MutableLiveData()

    init {
        val stationDao = AppDatabase.getDatabase(application).stationDao()
        repository = StationRepository(stationDao)
        allLocations = repository.readAllLocations
    }

    fun sortLocationsByDistance(location: Location) {
        val locations = allLocations.value
        if (locations != null) {
            val sortedLocations = locations.sortedBy { it.distanceTo(location) }
            this.sortedLocations.value = sortedLocations
        }
    }
}