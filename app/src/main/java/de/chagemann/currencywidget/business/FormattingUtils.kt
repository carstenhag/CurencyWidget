package de.chagemann.currencywidget.business

import android.icu.text.DecimalFormat
import javax.inject.Inject

class FormattingUtils @Inject constructor() : IFormattingUtils {

    private val decimalFormat by lazy {
        DecimalFormat.getInstance().apply {
            minimumFractionDigits = 0
            maximumFractionDigits = 4
        }
    }

    override fun formatAmount(currencyCode: String, amount: Double): String {
        return decimalFormat.format(amount)
    }
}
