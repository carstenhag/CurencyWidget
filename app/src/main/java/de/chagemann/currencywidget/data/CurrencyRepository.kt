package de.chagemann.currencywidget.data

import javax.inject.Inject


class CurrencyRepository @Inject constructor(
    private val currencyApi: ICurrencyApi
) : ICurrencyRepository {

}