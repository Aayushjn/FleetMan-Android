package com.aayush.fleetmanager.di.module

import androidx.room.Room
import com.aayush.fleetmanager.App
import com.aayush.fleetmanager.db.AppDatabase
import com.aayush.fleetmanager.db.dao.AlertDao
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
class RoomModule(app: App) {
    private val database: AppDatabase = Room.databaseBuilder(app, AppDatabase::class.java, "FleetManager.db")
        .build()

    @Provides
    @Singleton
    fun providesDatabase(): AppDatabase = database

    @Provides
    @Singleton
    fun providesAlertDao(): AlertDao = database.alertDao()
}