package de.chagemann.currencywidget.data

import kotlinx.collections.immutable.ImmutableMap

interface ICurrencyRepository {

    suspend fun fetchCurrencies(): ImmutableMap<String, String>?

    suspend fun fetchPricePairs(baseCurrency: String): PricePairs?
}
