package com.aayush.fleetmanager.di.component

import com.aayush.fleetmanager.di.module.AppModule
import com.aayush.fleetmanager.di.module.NetworkModule
import com.aayush.fleetmanager.di.module.RoomModule
import com.aayush.fleetmanager.ui.dashboard.DashboardFragment
import com.aayush.fleetmanager.ui.details.VehicleDetailsFragment
import com.aayush.fleetmanager.ui.login.LoginFragment
import com.aayush.fleetmanager.ui.register.RegisterFragment
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Component(modules = [AppModule::class, NetworkModule::class, RoomModule::class])
@Singleton
interface FragmentComponent {
    fun inject(fragment: DashboardFragment)
    fun inject(fragment: RegisterFragment)
    fun inject(fragment: LoginFragment)
    fun inject(fragment: VehicleDetailsFragment)
}