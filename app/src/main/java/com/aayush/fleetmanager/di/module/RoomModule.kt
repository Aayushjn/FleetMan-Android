package com.aayush.fleetmanager.di.module

import androidx.room.Room
import com.aayush.fleetmanager.App
import com.aayush.fleetmanager.R
import com.aayush.fleetmanager.db.AppDatabase
import com.aayush.fleetmanager.db.dao.AlertDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule(private val app: App) {
    @Provides
    @Singleton
    fun providesDatabase(): AppDatabase = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "${app.getString(R.string.app_name)}.db"
    )
        .build()

    @Provides
    @Singleton
    fun providesAlertDao(database: AppDatabase): AlertDao = database.alertDao()
}