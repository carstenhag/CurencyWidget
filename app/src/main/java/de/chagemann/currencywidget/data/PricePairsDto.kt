package de.chagemann.currencywidget.data

import kotlinx.serialization.Serializable

@Serializable(with = PricePairsDtoDeserializer::class)
data class PricePairsDto(
    val date: String,
    val pricePairs: Map<String, Double>
)
