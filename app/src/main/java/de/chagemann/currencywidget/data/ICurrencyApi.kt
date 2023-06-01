package de.chagemann.currencywidget.data

import retrofit2.Response
import java.util.Currency

interface ICurrencyApi {

    /**
     * Get the list of currencies supported by the api (Symbol aka Currency -> Name mapping).
     */
    suspend fun getCurrencyList(): Response<Unit>

    /**
     * € base -> 1€ = 1.15 USD
     */
    suspend fun getPricePairsForBaseCurrency(baseCurrency: Currency): Response<Unit>
}