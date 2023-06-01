package de.chagemann.currencywidget.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object PricePairsDtoDeserializer : KSerializer<PricePairsDto> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("PricePairsDto") {
        element("date", String.serializer().descriptor)
        element("rates", MapSerializer(String.serializer(), Double.serializer()).descriptor)
    }

    override fun deserialize(decoder: Decoder): PricePairsDto {
        var date = ""
        var rates = mapOf<String, Double>()

        val jsonDecoder = decoder as JsonDecoder
        jsonDecoder.decodeJsonElement().jsonObject.map {
            if (it.key == "date") {
                date = it.value.jsonPrimitive.content
            } else {
                val array = it.value
                rates = jsonDecoder.json.decodeFromJsonElement<Map<String, Double>>(array)
            }
        }

        return PricePairsDto(date, rates)
    }

    override fun serialize(encoder: Encoder, value: PricePairsDto) {
        throw SerializationException("Serialization is not supported for PricePairsDtoDeserializer.")
    }
}
