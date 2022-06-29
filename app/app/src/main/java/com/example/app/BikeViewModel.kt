package com.example.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.data.AppDatabase
import com.example.app.data.AppRepository
import com.example.app.data.Bike
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BikeViewModel(application: Application) : AndroidViewModel(application) {

    val allBikes: LiveData<List<Bike>>
    val repository: AppRepository

    init {
        val bikeDao = AppDatabase.getDatabase(application).bikeDao()
        repository = AppRepository(bikeDao)
        allBikes = repository.readAllBikes
    }

    fun insertAllBikes(vararg bikes: Bike) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertAllBikes(*bikes)
        }
    }
}
