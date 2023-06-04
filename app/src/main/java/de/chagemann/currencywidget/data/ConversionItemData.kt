package de.chagemann.currencywidget.data

data class ConversionItemData(
    val itemUuid: String,
    val baseCurrencyCode: String,
    val baseCurrencyAmount: Double,
    val formattedBaseCurrencyAmount: String,
    val targetCurrencyCode: String,
    val exchangeRate: Double,
    val targetCurrencyAmount: Double,
    val formattedTargetCurrencyAmount: String
) {

    // TODO: Probably remove this
    val itemSubline: String
        get() = "1 $baseCurrencyCode = $formattedTargetCurrencyAmount $targetCurrencyCode"

    fun copyAndSwapCurrencies(): ConversionItemData {
        return ConversionItemData(
            itemUuid = itemUuid,
            baseCurrencyCode = targetCurrencyCode,
            baseCurrencyAmount = baseCurrencyAmount * exchangeRate,
            formattedBaseCurrencyAmount = formattedTargetCurrencyAmount,
            targetCurrencyCode = baseCurrencyCode,
            exchangeRate = 1 / exchangeRate,
            targetCurrencyAmount = targetCurrencyAmount * (1/exchangeRate),
            formattedTargetCurrencyAmount = formattedBaseCurrencyAmount,
        )
    }
}
