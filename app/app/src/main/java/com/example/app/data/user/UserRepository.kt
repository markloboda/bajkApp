package com.example.app.data.user

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: User) = userDao.insertUser(user)

    suspend fun insertUserGetId(user: User): Long = userDao.insertUserGetId(user)

}