package com.example.app.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.app.data.AppDatabase
import com.example.app.data.bike.BikeRepository
import com.example.app.data.bike.Bike
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BikeViewModel(application: Application) : AndroidViewModel(application) {

    val repository: BikeRepository
    val allBikes: LiveData<List<Bike>>
    var bikeLive: MutableLiveData<Bike> = MutableLiveData()
    var bikesByStationId: MutableLiveData<List<Bike>> = MutableLiveData()

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
            val readBikes = repository.readBikesByStationId(stationId)
            bikesByStationId.postValue(readBikes)
        }
    }

    fun update(bike: Bike) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateBike(bike)
        }
    }

    fun update(vararg bikes: Bike) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateBikes(*bikes)
        }
    }
}
