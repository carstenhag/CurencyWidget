package de.chagemann.currencywidget.data

interface ICurrencyRepository {

    suspend fun fetchCurrencies(): Map<String, String>

    suspend fun fetchPricePairs(baseCurrency: String): PricePairs?
}
