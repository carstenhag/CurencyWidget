package de.chagemann.currencywidget

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.chagemann.currencywidget.business.FormattingUtils
import de.chagemann.currencywidget.business.IFormattingUtils
import de.chagemann.currencywidget.data.PricePairsDto
import de.chagemann.currencywidget.data.PricePairsDtoDeserializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter

@Module
@InstallIn(SingletonComponent::class)
class UtilsModule {

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true // for requests we want default values to be included
        isLenient = true // quoted booleans and unquoted strings are allowed
        coerceInputValues = true // coerce unknown enum values to default
        explicitNulls = false
        serializersModule = SerializersModule {
            contextual(PricePairsDto::class, PricePairsDtoDeserializer)
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    internal fun provideJsonConverterFactory(json: Json): Converter.Factory {
        val contentType = "application/json".toMediaType()
        return json.asConverterFactory(contentType)
    }

    @Provides
    fun provideDispatcherProvider(): IDispatcherProvider {
        return DispatcherProvider()
    }

    @Provides
    fun provideFormattingUtils(): IFormattingUtils {
        return FormattingUtils()
    }
}
