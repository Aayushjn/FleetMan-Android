package com.aayush.fleetmanager.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VehicleMinimal(
    @SerialName("vin")           val vin: String,
    @SerialName("license_plate") val licensePlate: String,
    @SerialName("model")         val model: String
)

enum class Role {
    ADMIN,
    MAINTENANCE,
    INSURANCE,
    DEV;
}