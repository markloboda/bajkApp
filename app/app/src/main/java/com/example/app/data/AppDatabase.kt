package com.example.app.data

import android.content.ContentValues
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.app.data.bike.Bike
import com.example.app.data.bike.BikeDao
import com.example.app.data.reservation.Reservation
import com.example.app.data.reservation.ReservationDao
import com.example.app.data.station.Station
import com.example.app.data.station.StationDao
import com.example.app.data.user.User
import com.example.app.data.user.UserDao

@Database(entities = [Bike::class, Station::class, User::class, Reservation::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bikeDao(): BikeDao
    abstract fun stationDao(): StationDao
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
                        // stations
                        val values = ContentValues()
                        for (i in 1..10) {
                            values.put("title", "Station $i")
                            values.put("latitude", 52.0 + i)
                            values.put("longitude", 13.0 + i)
                            values.put("spot_status", "1,1,1,1,1,0,0,0,0,0")
                            db.insert("location_table", 0, values)
                        }

                        values.clear()

                        // bikes
                        for (i in 1..10) {
                            for (j in 0..4) {
                                values.put("title", "Bike ${i} ${j}")
                                values.put("station_id", i)
                                values.put("spot_id", j)
                                db.insert("bike_table", 0, values)
                            }
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