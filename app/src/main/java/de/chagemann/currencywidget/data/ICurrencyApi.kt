package de.chagemann.currencywidget.data

import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Based on https://github.com/fawazahmed0/currency-api
 */
interface ICurrencyApi {

    /**
     * Get the list of currencies supported by the api (Symbol aka Currency -> Name mapping).
     */
    @GET("currencies.min.json")
    suspend fun getCurrencyList(): Map<String, String>

    /**
     * € base -> 1€ = 1.15 USD
     */
    @GET("currencies/{baseCurrency}.min.json")
    suspend fun getPricePairsForBaseCurrency(
        @Path("baseCurrency") baseCurrency: String
    ): PricePairsDto
}
