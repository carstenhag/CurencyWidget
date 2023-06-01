package de.chagemann.currencywidget

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.chagemann.currencywidget.data.CurrencyApi
import de.chagemann.currencywidget.data.CurrencyRepository
import de.chagemann.currencywidget.data.ICurrencyApi
import de.chagemann.currencywidget.data.ICurrencyRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class CurrencyModule {

    @Binds
    abstract fun provideICurrencyApi(currencyApi: CurrencyApi): ICurrencyApi

    @Binds
    abstract fun provideICurrencyRepository(currencyRepository: CurrencyRepository): ICurrencyRepository

}