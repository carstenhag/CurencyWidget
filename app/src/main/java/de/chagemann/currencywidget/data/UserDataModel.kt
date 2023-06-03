package de.chagemann.currencywidget.data

import de.chagemann.currencywidget.business.IFormattingUtils
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class UserDataModel(
    val conversionItemDataList: ImmutableList<ConversionItemData>
)

fun UserDataEntity.toModel(
    formattingUtils: IFormattingUtils,
    exchangeRate: Double // TODO: Have to figure this out
): UserDataModel {
    val mappedList = conversionItemEntityList.map {
        val targetAmount = it.baseCurrencyAmount * exchangeRate
        ConversionItemData(
            itemUuid = it.itemUuid,
            baseCurrencyCode = it.baseCurrencyCode,
            baseCurrencyAmount = it.baseCurrencyAmount,
            formattedBaseCurrencyAmount = formattingUtils.formatAmount(
                it.baseCurrencyCode,
                it.baseCurrencyAmount
            ),
            targetCurrencyCode = it.targetCurrencyCode,
            exchangeRate = exchangeRate,
            targetCurrencyAmount = targetAmount,
            formattedTargetCurrencyAmount = formattingUtils.formatAmount(
                it.targetCurrencyCode,
                targetAmount
            )
        )
    }.toImmutableList()

    return UserDataModel(mappedList)
}
