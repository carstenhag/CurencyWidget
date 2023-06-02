package de.chagemann.currencywidget.ui.currencyselection

import androidx.lifecycle.ViewModel

class CurrencySelectionViewModel: ViewModel() {
    fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.CloseScreen -> TODO()
        }
    }

    data class ViewState(
        val currencies: Map<String, String>
    )

    sealed class UiAction {
        object CloseScreen: UiAction()
    }
}
