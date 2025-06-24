package com.mobivery.fct25.data.serializer

import com.mobivery.fct25.data.model.AppVersionRemoteConfig
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object AppVersionSerializer : KSerializer<AppVersionRemoteConfig> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("AppVersion", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: AppVersionRemoteConfig) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): AppVersionRemoteConfig {
        val versionString = decoder.decodeString()
        return AppVersionRemoteConfig(versionString)
    }
}