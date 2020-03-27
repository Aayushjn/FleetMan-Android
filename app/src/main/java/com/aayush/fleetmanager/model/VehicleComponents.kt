package com.aayush.fleetmanager.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Brakes(
    @SerialName("fluid_level") val fluidLevel: Double,
    @SerialName("health")      val health: Double,
    @SerialName("temp")        val temp: Double,
    @SerialName("type")        val type: String
)

@Serializable
data class Engine(
    @SerialName("battery_capacity") val batteryCapacity: Double,
    @SerialName("current_level")    val currentLevel: Double,
    @SerialName("health")           val health: Double,
    @SerialName("temp")             val temp: Double
)

@Serializable
data class Fuel(
    @SerialName("capacity") val capacity: Double,
    @SerialName("health")   val health: Double,
    @SerialName("level")    val level: Double,
    @SerialName("rating")   val rating: String,
    @SerialName("type")     val type: String
)

@Serializable
data class Tyres(
    @SerialName("health")   val health: Double,
    @SerialName("pressure") val pressure: Double,
    @SerialName("rating")   val rating: String,
    @SerialName("temp")     val temp: Double,
    @SerialName("wear")     val wear: Double
)