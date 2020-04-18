package com.aayush.fleetmanager.di.component

import com.aayush.fleetmanager.di.module.AppModule
import com.aayush.fleetmanager.di.module.NetworkModule
import com.aayush.fleetmanager.di.module.RoomModule
import com.aayush.fleetmanager.util.android.service.DeleteAlertService
import com.aayush.fleetmanager.util.android.service.FirebaseService
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Component(modules = [AppModule::class, NetworkModule::class, RoomModule::class])
@Singleton
interface ServiceComponent {
    fun inject(service: DeleteAlertService)
    fun inject(service: FirebaseService)
}