package com.aayush.fleetmanager.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.db.dao.AlertDao
import com.aayush.fleetmanager.model.Alert

@Database(entities = [Alert::class], version = 2)
@TypeConverters(CustomTypeConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun alertDao(): AlertDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    context.getString(R.string.app_name)
                )
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
        }
    }
}