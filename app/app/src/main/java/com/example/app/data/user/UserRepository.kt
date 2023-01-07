package com.example.app.data.user

class UserRepository(private val userDao: UserDao) {

    fun getUser(id: Int) = userDao.getUser(id)

    fun getUserByPhone(phone: String) = userDao.getUserByPhone(phone)

    suspend fun insertUser(user: User): Long {
        return userDao.insertUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun updateUserBike(userId: Long, bikeId: Long) {
        userDao.updateUserBike(userId, bikeId)
    }
}