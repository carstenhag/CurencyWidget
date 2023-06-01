package de.chagemann.currencywidget

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.chagemann.currencywidget.data.CurrencyRepository
import de.chagemann.currencywidget.data.ICurrencyApi
import de.chagemann.currencywidget.data.ICurrencyRepository
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
class CurrencyProvidesModule {

    @Suppress("SpellCheckingInspection")
    private val baseUrl = "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/"

    @Provides
    fun provideICurrencyApi(
        client: OkHttpClient,
        converterFactory: Converter.Factory
    ): ICurrencyApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(converterFactory)
            .build()
            .create<ICurrencyApi>()
    }

}

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class CurrencyAbstractModule {
    @Binds
    abstract fun provideICurrencyRepository(currencyRepository: CurrencyRepository): ICurrencyRepository
}
