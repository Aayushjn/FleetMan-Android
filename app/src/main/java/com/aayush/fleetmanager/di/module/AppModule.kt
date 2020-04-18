package com.aayush.fleetmanager.di.module

import android.content.Context
import android.content.SharedPreferences
import com.aayush.fleetmanager.App
import com.aayush.fleetmanager.util.common.PREF_FILE_KEY
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {
    @Provides
    @Singleton
    fun provideApp(): App = app

    @Provides
    @Singleton
    fun providesSharedPreferences(): SharedPreferences =
        app.getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE)
}