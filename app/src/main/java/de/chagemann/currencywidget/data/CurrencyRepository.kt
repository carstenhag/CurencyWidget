package de.chagemann.currencywidget.data

import java.time.LocalDate
import javax.inject.Inject


class CurrencyRepository @Inject constructor(
    private val currencyApi: ICurrencyApi
) : ICurrencyRepository {
    override suspend fun fetchCurrencies(): Map<String, String> {
        val result = runCatching {
            currencyApi.getCurrencyList()
        }
        return result.getOrNull() ?: return mapOf()
    }

    override suspend fun fetchPricePairs(baseCurrency: String): PricePairs? {
        val result = runCatching {
            currencyApi.getPricePairsForBaseCurrency(baseCurrency)
        }
        val pricePairsDto = result.getOrNull() ?: return null

        return pricePairsDto.toBusinessObject()
    }

    private fun PricePairsDto.toBusinessObject(): PricePairs {
        return PricePairs(
            date = LocalDate.parse(this.date),
            pairs = this.pricePairs.map { PricePair(it.key, it.value) }
        )
    }
}
