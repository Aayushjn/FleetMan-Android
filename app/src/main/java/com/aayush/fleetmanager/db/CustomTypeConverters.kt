package com.aayush.fleetmanager.db

import android.os.ParcelUuid
import androidx.room.TypeConverter
import com.aayush.fleetmanager.model.AlertType

class CustomTypeConverters {
    @TypeConverter
    fun uuidFromString(value: String): ParcelUuid = ParcelUuid.fromString(value)

    @TypeConverter
    fun uuidToString(uuid: ParcelUuid): String = uuid.toString()

    @TypeConverter
    fun alertTypeToString(alertType: AlertType): String = alertType.toString()

    @TypeConverter
    fun alertTypeFromString(value: String): AlertType = AlertType.valueOf(value)
}