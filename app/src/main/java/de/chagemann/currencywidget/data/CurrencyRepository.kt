package de.chagemann.currencywidget.data

import androidx.datastore.core.DataStore
import de.chagemann.currencywidget.business.IFormattingUtils
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject


class CurrencyRepository @Inject constructor(
    private val currencyApi: ICurrencyApi,
    private val dataStore: DataStore<UserDataEntity>,
    private val formattingUtils: IFormattingUtils,
) : ICurrencyRepository {
    override suspend fun fetchCurrencies(): ImmutableMap<String, String>? {
        val result = runCatching {
            currencyApi.getCurrencyList()
        }
        return result.getOrNull()?.toImmutableMap()
    }

    override suspend fun fetchPricePairs(baseCurrency: String): PricePairs? {
        val result = runCatching {
            currencyApi.getPricePairsForBaseCurrency(baseCurrency)
        }
        val pricePairsDto = result.getOrNull() ?: return null

        return pricePairsDto.toBusinessObject()
    }

    override val userData: Flow<UserDataModel>
        get() = dataStore.data.map {
            it.toModel(formattingUtils, 1.07)
        }

    override suspend fun addConversionItem(
        baseCurrencyCode: String,
        baseCurrencyAmount: Double,
        targetCurrencyCode: String
    ) {
        dataStore.updateData {
            it.toBuilder().addConversionItemEntity(
                ConversionItemEntity.newBuilder()
                    .setItemUuid(
                        UUID.randomUUID().toString(),
                    )
                    .setBaseCurrencyCode(baseCurrencyCode)
                    .setBaseCurrencyAmount(baseCurrencyAmount)
                    .setTargetCurrencyCode(targetCurrencyCode)
                    .build()
            ).build()
        }
    }

    override suspend fun swapConversionItem(item: ConversionItemData) {
        val swappedItem = item.copyAndSwapCurrencies()

        dataStore.updateData { userDataEntity ->
            val index = conversionItemDataIndexOf(userDataEntity, item)
            val conversionItemEntity = userDataEntity.getConversionItemEntity(index)
            val updatedConversionItemEntity = conversionItemEntity.toBuilder()
                .setBaseCurrencyCode(swappedItem.baseCurrencyCode)
                .setBaseCurrencyAmount(swappedItem.baseCurrencyAmount)
                .setTargetCurrencyCode(swappedItem.targetCurrencyCode)

            userDataEntity
                .toBuilder()
                .setConversionItemEntity(index, updatedConversionItemEntity)
                .build()
        }
    }

    override suspend fun deleteConversionItem(deletionItem: ConversionItemData) {
        dataStore.updateData { userDataEntity ->
            val index = conversionItemDataIndexOf(userDataEntity, deletionItem)
            userDataEntity.toBuilder().removeConversionItemEntity(index).build()
        }
    }

    private fun conversionItemDataIndexOf(
        userDataEntity: UserDataEntity,
        deletionItem: ConversionItemData
    ) = userDataEntity.conversionItemEntityList.indexOfFirst {
        it.itemUuid == deletionItem.itemUuid
    }

    private fun PricePairsDto.toBusinessObject(): PricePairs {
        return PricePairs(
            date = LocalDate.parse(this.date),
            pairs = this.pricePairs.map { PricePair(it.key, it.value) }
        )
    }
}
