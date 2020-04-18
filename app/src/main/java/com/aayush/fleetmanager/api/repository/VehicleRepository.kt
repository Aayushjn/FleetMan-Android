package com.aayush.fleetmanager.api.repository

import com.aayush.fleetmanager.api.RestApi
import com.aayush.fleetmanager.util.common.State
import com.aayush.fleetmanager.util.common.State.*
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VehicleRepository @Inject constructor(private val restApi: RestApi) {
    fun loadVehicleMinimals(): Flow<State> = flow {
        emit(Loading)
        when(val response = restApi.getVehicleMinimals()) {
            is NetworkResponse.Success -> emit(Success(response.body.mapped()))
            is NetworkResponse.NetworkError -> emit(Failure(response.error.message ?: "Network Error"))
            is NetworkResponse.ServerError -> emit(Failure(response.body?.message ?: "Server Error"))
        }
    }

    fun loadVehicleCount(): Flow<State> = flow {
        emit(Loading)
        when(val response = restApi.getVehicleCount()) {
            is NetworkResponse.Success -> emit(Success(response.body))
            is NetworkResponse.NetworkError -> emit(Failure(response.error.message ?: "Network Error"))
            is NetworkResponse.ServerError -> emit(Failure(response.body?.message ?: "Server Error"))
        }
    }

    fun loadVehicle(vin: String, email: String): Flow<State> = flow {
        emit(Loading)
        if (vin.length == 17) {
            when(val response = restApi.getVehicleByVinOrLicensePlate(vin, null, email)) {
                is NetworkResponse.Success -> emit(Success(response.body))
                is NetworkResponse.NetworkError -> emit(Failure(response.error.message ?: "Network Error"))
                is NetworkResponse.ServerError -> emit(Failure(response.body?.message ?: "Server Error"))
            }
        } else {
            when(val response = restApi.getVehicleByVinOrLicensePlate(null, vin, email)) {
                is NetworkResponse.Success -> emit(Success(response.body))
                is NetworkResponse.NetworkError -> emit(Failure(response.error.message ?: "Network Error"))
                is NetworkResponse.ServerError -> emit(Failure(response.body?.message ?: "Server Error"))
            }
        }
    }
}