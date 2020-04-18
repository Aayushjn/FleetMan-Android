package com.aayush.fleetmanager.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aayush.fleetmanager.db.dao.AlertDao
import com.aayush.fleetmanager.model.Alert
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Database(entities = [Alert::class], version = 1)
@TypeConverters(CustomTypeConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun alertDao(): AlertDao
}