package de.chagemann.currencywidget.data

import retrofit2.Response
import java.util.Currency
import javax.inject.Inject

class CurrencyApi @Inject constructor() : ICurrencyApi {
    override suspend fun getCurrencyList(): Response<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getPricePairsForBaseCurrency(baseCurrency: Currency): Response<Unit> {
        TODO("Not yet implemented")
    }
}