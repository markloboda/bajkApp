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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StationViewModel(application: Application) : AndroidViewModel(application) {

    val allStations: LiveData<List<Station>>
    val repository: StationRepository
    val sortedStations: MutableLiveData<List<Station>> = MutableLiveData()
    val stationLive: MutableLiveData<Station> = MutableLiveData()

    init {
        val stationDao = AppDatabase.getDatabase(application).stationDao()
        repository = StationRepository(stationDao)
        allStations = repository.readAllLocations
    }

    fun sortLocationsByDistance(location: Location) {
        val locations = allStations.value
        if (locations != null) {
            val sortedLocations = locations.sortedBy { it.distanceTo(location) }
            this.sortedStations.postValue(sortedLocations)
        }
    }

    fun readStationById(stationId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val station = repository.readStationById(stationId)
            stationLive.postValue(station)
        }
    }

    fun updateStation(station: Station) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateStation(station)
        }
    }
}