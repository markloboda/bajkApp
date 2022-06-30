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

    val allBikes: LiveData<List<Bike>>
    val repository: BikeRepository
    var bikeLive: MutableLiveData<Bike> = MutableLiveData()

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

    fun updateBike(bike: Bike) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateBike(bike)
        }
    }
}
