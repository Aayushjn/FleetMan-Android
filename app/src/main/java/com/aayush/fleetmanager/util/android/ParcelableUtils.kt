package com.aayush.fleetmanager.util.android

import android.os.Parcel
import android.os.Parcelable
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

interface KParcelable: Parcelable {
    override fun writeToParcel(parcel: Parcel, flags: Int)
    override fun describeContents(): Int = 0
}

fun Parcel.readLocalDateTime(): LocalDateTime = LocalDateTime.parse(readString())

fun Parcel.writeLocalDateTime(value: LocalDateTime) = writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))

inline fun <reified T: Enum<T>> Parcel.readEnum(): T = enumValues<T>()[readInt()]

inline fun <reified T: Enum<T>> Parcel.writeEnum(value: T) = writeInt(value.ordinal)

fun Parcel.readBool(): Boolean = readInt() != 0

fun Parcel.writeBool(value: Boolean) = writeInt(if (value) 1 else 0)