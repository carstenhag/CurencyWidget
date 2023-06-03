package de.chagemann.currencywidget.data

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.flow.Flow

interface ICurrencyRepository {

    suspend fun fetchCurrencies(): ImmutableMap<String, String>?

    suspend fun fetchPricePairs(baseCurrency: String): PricePairs?

    val userData: Flow<UserDataModel>

    suspend fun addConversionItem(
        baseCurrencyCode: String,
        baseCurrencyAmount: Double,
        targetCurrencyCode: String,
    )
}
