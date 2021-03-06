package com.aayush.fleetmanager.api.repository

import com.aayush.fleetmanager.api.RestApi
import com.aayush.fleetmanager.util.common.State
import com.aayush.fleetmanager.util.common.State.*
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepository @Inject constructor(private val restApi: RestApi) {
    fun registerUser(name: String, email: String, password: String, role: String): Flow<State> = flow {
        emit(Loading)
        when(val response = restApi.registerUser(name, email, password, role)) {
            is NetworkResponse.Success -> emit(Success(response.body))
            is NetworkResponse.NetworkError -> emit(Failure(response.error.message ?: "Network Error"))
            is NetworkResponse.ServerError -> emit(Failure(response.body?.message ?: "Server Error"))
        }
    }

    fun loginUser(email: String, password: String): Flow<State> = flow {
        emit(Loading)
        when(val response = restApi.loginUser(email, password)) {
            is NetworkResponse.Success -> emit(Success(response.body))
            is NetworkResponse.NetworkError -> emit(Failure(response.error.message ?: "Network Error"))
            is NetworkResponse.ServerError -> emit(Failure(response.body?.message ?: "Server Error"))
        }
    }

    fun logoutUser(email: String): Flow<State> = flow {
        emit(Loading)
        when(val response = restApi.logoutUser(email)) {
            is NetworkResponse.Success -> emit(Success(response.body))
            is NetworkResponse.NetworkError -> emit(Failure(response.error.message ?: "Network Error"))
            is NetworkResponse.ServerError -> {
                if (response.code == 204) {
                    emit(Success(response.body))
                } else {
                    emit(Failure(response.body?.message ?: response.toString()))
                }
            }
        }
    }
}