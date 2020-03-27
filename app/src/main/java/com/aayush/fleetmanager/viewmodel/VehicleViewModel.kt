package com.aayush.fleetmanager.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aayush.fleetmanager.api.repository.VehicleRepository
import com.aayush.fleetmanager.util.common.Result

class VehicleViewModel(
    vehicleRepository: VehicleRepository,
    vin: String,
    email: String
): ViewModel() {
    private val _vehicleResult: LiveData<Result> = vehicleRepository
        .loadVehicle(vin, email)
        .asLiveData(viewModelScope.coroutineContext)

    val vehicleResult: LiveData<Result>
        get() = _vehicleResult
}