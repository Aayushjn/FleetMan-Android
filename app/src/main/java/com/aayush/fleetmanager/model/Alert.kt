package com.aayush.fleetmanager.model

import android.os.ParcelUuid
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import timber.log.Timber
import java.util.*

@Serializable
@Entity(tableName = "alert")
data class Alert(
    @SerialName("vin")           val vin: String,
    @ColumnInfo(name = "license_plate")
    @SerialName("license_plate") val licensePlate: String,
    @SerialName("text")          val text: String,
    @SerialName("type")          val type: AlertType,
    @Transient @PrimaryKey             val id: ParcelUuid = ParcelUuid(UUID.randomUUID())
) {
    companion object {
        @JvmStatic
        fun fromMap(map: Map<String, String>): Alert? = try {
            Alert(
                checkNotNull(map["vin"]),
                checkNotNull(map["license_plate"]),
                checkNotNull(map["text"]),
                AlertType.valueOf(checkNotNull(map["type"]))
            )
        } catch (e: IllegalArgumentException) {
            Timber.d("Invalid alert type: ${map["type"]}")
            null
        }
    }
}

enum class AlertType {
    SEVERE,
    WARNING;
}