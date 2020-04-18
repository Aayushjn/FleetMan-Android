package com.aayush.fleetmanager.di.component

import com.aayush.fleetmanager.di.module.AppModule
import com.aayush.fleetmanager.view.MainActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface ActivityComponent {
    fun inject(activity: MainActivity)
}