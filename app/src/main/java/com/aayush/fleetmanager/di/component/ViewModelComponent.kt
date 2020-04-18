package com.aayush.fleetmanager.di.component

import com.aayush.fleetmanager.di.module.AppModule
import com.aayush.fleetmanager.di.module.NetworkModule
import com.aayush.fleetmanager.di.module.RoomModule
import com.aayush.fleetmanager.ui.dashboard.DashboardViewModel
import com.aayush.fleetmanager.ui.details.VehicleDetailsViewModel
import com.aayush.fleetmanager.ui.login.LoginViewModel
import com.aayush.fleetmanager.ui.register.RegisterViewModel
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Component(modules = [AppModule::class, NetworkModule::class, RoomModule::class])
@Singleton
interface ViewModelComponent {
    fun inject(viewModel: DashboardViewModel)
    fun inject(viewModel: RegisterViewModel)
    fun inject(viewModel: LoginViewModel)
    fun inject(viewModel: VehicleDetailsViewModel)
}