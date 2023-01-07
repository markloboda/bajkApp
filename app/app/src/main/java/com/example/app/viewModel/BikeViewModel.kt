package com.example.app.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.app.data.AppDatabase
import com.example.app.data.bike.Bike
import com.example.app.data.bike.BikeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BikeViewModel(application: Application) : AndroidViewModel(application) {

    val repository: BikeRepository
    val allBikes: LiveData<List<Bike>>
    var bikeLive: MutableLiveData<Bike> = MutableLiveData()
    var bikesByStationId: MutableLiveData<List<Bike?>> = MutableLiveData()
    var bikeByStationIdAndSpotIndex: MutableLiveData<Bike?> = MutableLiveData()
    var updateFlag: MutableLiveData<Boolean> = MutableLiveData()

    init {
        val bikeDao = AppDatabase.getDatabase(application).bikeDao()
        repository = BikeRepository(bikeDao)
        allBikes = repository.readAllBikes
    }

    fun readBikeById(bikeId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val readBike = repository.readBikeById(bikeId)
            bikeLive.postValue(readBike!!)
        }
    }

    fun readBikesByStationId(stationId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            bikesByStationId.postValue(repository.readBikesByStationId(stationId))
        }
    }

    fun readBikeByStationIdAndSpotIndex(stationId: Long, spotIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val bike = repository.readBikeByStationIdAndSpotIndex(stationId, spotIndex)
            bikeByStationIdAndSpotIndex.postValue(bike)
        }
    }

    fun update(bike: Bike) {
        updateFlag.postValue(false)
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateBike(bike)
            updateFlag.postValue(true)
        }
    }

    fun update(vararg bikes: Bike) {
        updateFlag.postValue(false)
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateBikes(*bikes)
            updateFlag.postValue(true)
        }
    }

    fun updateBike(bikeId: Long, stationId: Long, spotIndex: Int) {
        updateFlag.postValue(false)
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateBike(bikeId, stationId, spotIndex)
            updateFlag.postValue(true)
        }
    }
}
