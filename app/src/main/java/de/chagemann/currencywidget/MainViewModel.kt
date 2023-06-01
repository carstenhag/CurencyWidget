package de.chagemann.currencywidget

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.chagemann.currencywidget.data.ICurrencyRepository
import de.chagemann.currencywidget.data.PricePairs
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
            baseCurrency = "eur"
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

    data class ViewState(
        val currencies: Map<String, String>,
        val pricePairs: PricePairs?,
        val baseCurrency: String
    )

}
