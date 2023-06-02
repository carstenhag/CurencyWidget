package de.chagemann.currencywidget

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.chagemann.currencywidget.data.ICurrencyRepository
import de.chagemann.currencywidget.data.PricePairs
import de.chagemann.currencywidget.ui.ConversionItemData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class MainViewModel @Inject constructor(
    private val currencyRepository: ICurrencyRepository,
    private val dispatcherProvider: IDispatcherProvider,
) : ViewModel() {

    private var job: Job? = null
        set(value) {
            job?.cancel()
            field = value
        }

    private val coroutineContext: CoroutineContext
        get() {
            val currentJob = job ?: Job().also { job = it }
            return currentJob + dispatcherProvider.IO
        }

    private val coroutineScope = CoroutineScope(coroutineContext)

    private val _viewState = MutableStateFlow(
        ViewState(
            currencies = mapOf(),
            pricePairs = null,
            baseCurrency = "eur",
            conversionItemDataList = listOf()
        )
    )
    val viewState = _viewState.asStateFlow()

    fun loadDataForNewBaseCurrency(baseCurrency: String) {
        val lowercaseBaseCurrency = baseCurrency.lowercase()

        coroutineScope.launch {
            val pricePairs = currencyRepository.fetchPricePairs(lowercaseBaseCurrency)
            if (pricePairs != null) {
                _viewState.tryEmit(
                    viewState.value.copy(
                        pricePairs = pricePairs,
                        baseCurrency = lowercaseBaseCurrency
                    )
                )
            }
        }
    }

    fun onAction(action: UiAction) {
        when (action) {
            is UiAction.AddNewThingy -> TODO() // open picker to select 2 currency codes
            is UiAction.OpenPicker -> TODO() // open picker to only select one currency code
            is UiAction.SwapCurrency -> TODO() // swap values, persist
            is UiAction.ShowDeletionDialog -> TODO() // show deletion dialog for a single item
        }
    }

    data class ViewState(
        val currencies: Map<String, String>,
        val pricePairs: PricePairs?,
        val baseCurrency: String,
        val conversionItemDataList: List<ConversionItemData>
    )

    sealed class UiAction {
        data class SwapCurrency(val conversionItemData: ConversionItemData) : UiAction()
        data class OpenPicker(
            val conversionItemData: ConversionItemData,
            val origin: Origin
        ) : UiAction() {
            enum class Origin {
                BASE_CURRENCY,
                TARGET_CURRENCY
            }
        }

        data class ShowDeletionDialog(val conversionItemData: ConversionItemData) : UiAction()

        object AddNewThingy : UiAction()
    }

}
