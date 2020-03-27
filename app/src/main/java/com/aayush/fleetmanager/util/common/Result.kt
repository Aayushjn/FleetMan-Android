package com.aayush.fleetmanager.util.common

sealed class Result

data class Success<T>(val data: T): Result()
data class Failure(val message: String): Result()
object Loading : Result()