package com.aayush.fleetmanager.api.repository

import com.aayush.fleetmanager.api.RestApi
import com.aayush.fleetmanager.util.common.*
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VehicleRepository private constructor(private val restApi: RestApi) {
    fun loadVehicleMinimals(): Flow<Result> = flow {
        emit(Loading)
        when(val response = restApi.getVehicleMinimals()) {
            is NetworkResponse.Success -> emit(Success(response.body.mapped()))
            is NetworkResponse.NetworkError -> emit(Failure(response.error.message ?: "Network Error"))
            is NetworkResponse.ServerError -> emit(Failure(response.body?.message ?: "Server Error"))
        }
    }

    fun loadVehicleCount(): Flow<Result> = flow {
        emit(Loading)
        when(val response = restApi.getVehicleCount()) {
            is NetworkResponse.Success -> emit(Success(response.body))
            is NetworkResponse.NetworkError -> emit(Failure(response.error.message ?: "Network Error"))
            is NetworkResponse.ServerError -> emit(Failure(response.body?.message ?: "Server Error"))
        }
    }

    fun loadVehicle(vin: String, email: String): Flow<Result> = flow {
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

    companion object: SingletonHolder<VehicleRepository, RestApi>(::VehicleRepository)
}