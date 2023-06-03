package de.chagemann.currencywidget.business

interface IFormattingUtils {
    fun formatAmount(currencyCode: String, amount: Double): String
}
