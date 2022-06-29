package com.example.app.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Bike::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bikeDao(): BikeDao

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

                        db.beginTransaction()
                        val values = ContentValues()
                        for (i in 1..7) {
                            values.put("title", "Bajk $i")
                            values.put("status", true)
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