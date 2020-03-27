package com.aayush.fleetmanager.util.android

import kotlinx.serialization.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

@Serializer(LocalDateTime::class)
object LocalDateTimeSerializer: KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor = SerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    }

    override fun deserialize(decoder: Decoder): LocalDateTime =
        LocalDateTime.parse(decoder.decodeString())
}