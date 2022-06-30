package com.example.app.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.app.data.AppDatabase
import com.example.app.data.user.User
import com.example.app.data.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {

    private val repository: UserRepository

    var userLive: MutableLiveData<User?> = MutableLiveData()

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun getUser(ime: String, priimek: String, sektor: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = repository.getUser(ime, priimek, sektor)
            userLive.postValue(user)
        }
    }

    fun insertUser(ime: String, priimek: String, sektor: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = User(ime, priimek, sektor)
            repository.insertUser(user)
        }
    }
}