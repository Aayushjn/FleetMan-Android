package com.aayush.fleetmanager.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("message") val message: String
)

@Serializable
data class MinimalModel(
    @SerialName("Hatchbacks") val hatchbacks: List<VehicleMinimal>,
    @SerialName("SUVs")       val suvs: List<VehicleMinimal>,
    @SerialName("Sedans")     val sedans: List<VehicleMinimal>
) {
    fun mapped(): HashMap<String, List<VehicleMinimal>> = hashMapOf(
        "Hatchbacks" to hatchbacks,
        "Sedans" to sedans,
        "SUVs" to suvs
    )
}

@Serializable
data class Vehicle(
    @SerialName("brakes")         val brakes: Brakes?,
    @SerialName("driver")         val driver: String?,
    @SerialName("engine")         val engine: Engine?,
    @SerialName("fuel")           val fuel: Fuel?,
    @SerialName("license_plate")  val licensePlate: String?,
    @SerialName("model")          val model: String?,
    @SerialName("overall_health") val overallHealth: Double?,
    @SerialName("type")           val type: String?,
    @SerialName("tyres")          val tyres: Tyres?,
    @SerialName("vin")            val vin: String?
)

@Serializable
data class VehicleCount(
    @SerialName("hatchbacks") val hatchbacks: Int,
    @SerialName("sedans")     val sedans: Int,
    @SerialName("suvs")       val suvs: Int
)

@Serializable
data class User(
    @SerialName("name") val name: String,
    @SerialName("email_id") val emailId: String,
    @SerialName("role") val role: Role
)