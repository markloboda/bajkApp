package com.example.app.data.user

class UserRepository(private val userDao: UserDao) {

    suspend fun getUser(ime: String, priimek: String, sektor: String) = userDao.getUser(ime.lowercase(), priimek.lowercase(), sektor.lowercase())

    suspend fun insertUser(user: User) = userDao.insertUser(user)
}