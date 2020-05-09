package com.aayush.fleetmanager.di.component

import com.aayush.fleetmanager.di.module.AppModule
import com.aayush.fleetmanager.di.module.NetworkModule
import com.aayush.fleetmanager.di.module.RoomModule
import com.aayush.fleetmanager.ui.fragment.dashboard.DashboardViewModel
import com.aayush.fleetmanager.ui.fragment.details.VehicleDetailsViewModel
import com.aayush.fleetmanager.ui.fragment.login.LoginViewModel
import com.aayush.fleetmanager.ui.fragment.register.RegisterViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, NetworkModule::class, RoomModule::class])
@Singleton
interface ViewModelComponent {
    fun inject(viewModel: DashboardViewModel)
    fun inject(viewModel: RegisterViewModel)
    fun inject(viewModel: LoginViewModel)
    fun inject(viewModel: VehicleDetailsViewModel)
}