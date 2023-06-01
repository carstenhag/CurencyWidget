package de.chagemann.currencywidget.data

import java.time.LocalDate

data class PricePairs(
    val date: LocalDate,
    val pairs: List<PricePair>
)
