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
    var userIdLive: MutableLiveData<Long> = MutableLiveData()
    var userByPhone: MutableLiveData<User?> = MutableLiveData()
    var updateFlag: MutableLiveData<Boolean> = MutableLiveData()

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun getUser(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            userLive.postValue(repository.getUser(id))
        }
    }

    fun getUserByPhone(phone: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userByPhone.postValue(repository.getUserByPhone(phone))
        }
    }

    fun insertUser(username: String, ime: String, priimek: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = User(username, ime, priimek, -1)
            val userId = repository.insertUser(user)
            userIdLive.postValue(userId)
        }
    }

    fun insertUser(user: User) {
        this.insertUser(user.phone, user.name, user.surname)
    }

    fun updateUser(user: User) {
        updateFlag.postValue(false)
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
            updateFlag.postValue(true)
        }
    }

    fun updateUserBike(userId: Long, bikeId: Long) {
        updateFlag.postValue(false)
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUserBike(userId, bikeId)
            updateFlag.postValue(true)
        }
    }
}