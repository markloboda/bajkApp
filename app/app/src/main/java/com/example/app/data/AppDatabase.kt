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
                        // create a list of stations
                        val spotStatusStr = "1,1,1,1,1,1,0,0,0,0"
                        val stations = mutableListOf(
                            Station("Station 1", 52.520008, 13.404954, spotStatusStr),
                            Station("Station 2", 52.520008, 13.404954, spotStatusStr),
                            Station("Station 3", 52.520008, 13.404954, spotStatusStr),
                            Station("Station 4", 52.520008, 13.404954, spotStatusStr),
                            Station("Station 5", 52.520008, 13.404954, spotStatusStr),
                            Station("Station 6", 52.520008, 13.404954, spotStatusStr),
                            Station("Station 7", 52.520008, 13.404954, spotStatusStr),
                            Station("Station 8", 52.520008, 13.404, spotStatusStr)
                        )

                        // create a list of bikes
                        val bikes = mutableListOf<Bike>()
                        for (station in stations) {
                            for (i in 0..5) {
                                bikes.add(Bike("Bajk ${station.id * i}", station.id, i.toLong()))
                            }
                        }

                        // insert stations and bikes into the database
                        db.beginTransaction()

                        val values = ContentValues()

                        for (station in stations) {
                            values.put("title", station.title)
                            values.put("latitude", station.latitude)
                            values.put("longitude", station.longitude)
                            values.put("spot_status", station.spotStatus)
                            db.insert("location_table", 0, values)
                        }

                        values.clear()
                        for (bike in bikes) {
                            values.put("title", bike.title)
                            values.put("station_id", bike.locationId)
                            values.put("spot_id", bike.spotId)
                            db.insert("bike_table", 0, values)
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