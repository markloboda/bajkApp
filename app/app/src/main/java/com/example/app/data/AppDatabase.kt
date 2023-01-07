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

//private const val DATABASE_NAME = "dummy_database"
private const val DATABASE_NAME = "ljubljana"

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
            if (DATABASE_NAME == "dummy_database") {
                return getDummyDatabase(context)
            } else {
                return getLJDatabase(context)
            }
        }

        private fun getDummyDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "database"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        // generate some dummy data
                        db.beginTransaction()
                        // stations
                        val values = ContentValues()
                        for (i in 1..10) {
                            values.put("address", "address $i")
                            values.put("title", "Station $i")
                            values.put("latitude", 52.0 + i)
                            values.put("longitude", 13.0 + i)
                            values.put("parking_count", 10)
                            db.insert("location_table", 0, values)
                        }

                        values.clear()

                        // bikes
                        for (i in 1..10) {
                            for (j in 0..4) {
                                values.put("title", "Bike ${i} ${j}")
                                values.put("station_id", i)
                                values.put("spot_index", j)
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

        private fun getLJDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "database"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        // generate some dummy data
                        db.beginTransaction()
                        // stations
                        val values = ContentValues()

                        values.put("address", "Ameriška ulica 2, 1000 Ljubljana")
                        values.put("title", "BTC Ljubljana")
                        values.put("latitude", 46.056946)
                        values.put("longitude", 14.505751)
                        values.put("parking_count", 10)
                        db.insert("location_table", 0, values)

                        values.put("address", "Šmartinska c. 102, 1000 Ljubljana")
                        values.put("title", "Mercator HM Šmartinska")
                        values.put("latitude", 46.067367)
                        values.put("longitude", 14.536122)
                        values.put("parking_count", 10)
                        db.insert("location_table", 0, values)

                        values.put("address", "Resljeva cesta 2, 1000 Ljubljana")
                        values.put("title", "Zmajski most")
                        values.put("latitude", 46.051722)
                        values.put("longitude", 14.510541)
                        values.put("parking_count", 10)
                        db.insert("location_table", 0, values)

                        values.put("address", "Resljeva cesta 12, 1000 Ljubljana")
                        values.put("title", "Gimnazija Ledina")
                        values.put("latitude", 46.051722)
                        values.put("longitude", 14.510541)
                        values.put("parking_count", 10)
                        db.insert("location_table", 0, values)

                        values.put("address", "Večna pot 113, 1000 Ljubljana")
                        values.put("title", "FRI")
                        values.put("latitude", 46.050688)
                        values.put("longitude", 14.468956)
                        values.put("parking_count", 10)
                        db.insert("location_table", 0, values)

                        values.clear()

                        // bikes
                        for (i in 1..5) {
                            for (j in 0..4) {
                                values.put("title", "Bike ${i} ${j}")
                                values.put("station_id", i)
                                values.put("spot_index", j)
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
