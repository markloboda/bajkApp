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

    var userId: MutableLiveData<Int> = MutableLiveData()

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) { repository.insertUser(user) }
    }

    fun insertUserGetId(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = repository.insertUserGetId(user).toInt()
            userId.postValue(id)
        }
    }
}