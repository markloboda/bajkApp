package com.example.app.data.user

import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM user_table WHERE id=:id")
    fun getUser(id: Int): User?

    @Query("SELECT * FROM user_table WHERE phone=:phone")
    fun getUserByPhone(phone: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(user: User)

    @Query("UPDATE user_table SET bike_id=:bikeId WHERE id=:userId")
    suspend fun updateUserBike(userId: Long, bikeId: Long)
}