package com.example.app.data.user

import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM user_table WHERE id=:id")
    fun getUser(id: Int): User?

    @Query("SELECT * FROM user_table WHERE ime=:ime AND priimek=:priimek AND sektor=:sektor")
    suspend fun getUser(ime: String, priimek: String, sektor: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long
}