package com.example.app.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.app.data.bike.Bike
import com.example.app.data.bike.BikeDao
import com.example.app.data.location.Location
import com.example.app.data.location.LocationDao
import com.example.app.data.reservation.Reservation
import com.example.app.data.reservation.ReservationDao
import com.example.app.data.user.User
import com.example.app.data.user.UserDao

@Database(entities = [Bike::class, Location::class, User::class, Reservation::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bikeDao(): BikeDao
    abstract fun locationDao(): LocationDao
    abstract fun userDao(): UserDao
    abstract fun reservationDao(): ReservationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        // generate some dummy data
                        db.beginTransaction()
                        // locations
                        val values = ContentValues()
                        for (i in 1..10) {
                            values.put("title", "Lokacija $i")
                            db.insert("location_table", SQLiteDatabase.CONFLICT_REPLACE, values)
                        }
                        values.clear()

                        // bikes
                        /// generate 50 bikes and assign them to a random location
                        for (i in 1..50) {
                            values.put("title", "Bicikl $i")
                            values.put("location_id", (1..10).random())
                            values.put("status", (0..1).random())
                            db.insert("bike_table", SQLiteDatabase.CONFLICT_REPLACE, values)
                        }
                        db.setTransactionSuccessful()
                        db.endTransaction()
                    }
                }).build()
                INSTANCE = instance

                return instance
            }
        }
    }
}