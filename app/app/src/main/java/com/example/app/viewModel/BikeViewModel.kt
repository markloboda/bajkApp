package com.example.app.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.app.data.AppDatabase
import com.example.app.data.bike.BikeRepository
import com.example.app.data.bike.Bike
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BikeViewModel(application: Application) : AndroidViewModel(application) {

    val allBikes: LiveData<List<Bike>>
    val repository: BikeRepository

    init {
        val bikeDao = AppDatabase.getDatabase(application).bikeDao()
        repository = BikeRepository(bikeDao)
        allBikes = repository.readAllBikes
    }

    fun insertAllBikes(vararg bikes: Bike) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAllBikes(*bikes)
        }
    }
}
