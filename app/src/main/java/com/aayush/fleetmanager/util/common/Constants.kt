package com.aayush.fleetmanager.util.common

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.util.*

const val BASE_URL = "http://192.168.43.211:8000/"
const val CACHE_SIZE: Long = 10 * 1024 * 1024

const val CHANNEL_ALERTS_ID = "AlertsChannelId"
const val CHANNEL_ALERTS = "AlertsChannel"

const val PREF_FILE_KEY = "com.aayush.fleetmanager.PREF_FILE_KEY"
const val PREF_EMAIL = "EmailPreference"
const val PREF_ROLE = "RolePreference"
const val PREF_IS_LOGGED_IN = "IsLoggedInPreference"

const val EXTRA_EMAIL = "EmailExtra"
const val EXTRA_ROLE = "RoleExtra"
const val EXTRA_VIN = "VINExtra"
const val EXTRA_ALERT_ID = "AlertIdExtra"

const val EXTRA_BRAKE_FLUID_LEVEL = "BrakeFluidLevelExtra"
const val EXTRA_BRAKE_TEMP = "BrakeTempExtra"
const val EXTRA_BRAKE_TYPE = "BrakeTypeExtra"

const val EXTRA_ENGINE_BATTERY_CAPACITY = "EngineBatteryCapacityExtra"
const val EXTRA_ENGINE_TEMP = "EngineTempExtra"

const val EXTRA_FUEL_CAPACITY = "FuelCapacityExtra"
const val EXTRA_FUEL_LEVEL = "FuelLevelExtra"
const val EXTRA_FUEL_RATING = "FuelRatingExtra"
const val EXTRA_FUEL_TYPE = "FuelTypeExtra"

const val EXTRA_TYRE_PRESSURE = "TyrePressureExtra"
const val EXTRA_TYRE_RATING = "TyreRatingExtra"
const val EXTRA_TYRE_TEMP = "TyreTempExtra"
const val EXTRA_TYRE_WEAR = "TyreWearExtra"

val JSON: Json = Json(JsonConfiguration.Stable.copy(prettyPrint = true))

val DEFAULT_LOCALE: Locale = Locale.US