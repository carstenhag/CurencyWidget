package de.chagemann.currencywidget.ui.currencyselection

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.chagemann.currencywidget.IDispatcherProvider
import de.chagemann.currencywidget.data.ICurrencyRepository
import de.chagemann.currencywidget.ui.currencyselection.CurrencySelectionViewModel.ViewState.CurrenciesState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class CurrencySelectionViewModel @Inject constructor(
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
            currenciesState = CurrenciesState.Loading
        )
    )
    val viewState = _viewState.asStateFlow()

    fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.CloseScreen -> TODO()
        }
    }

    fun loadCurrencies() {
        _viewState.tryEmit(viewState.value.copy(currenciesState = CurrenciesState.Loading))
        coroutineScope.launch {
            val result = runCatching { currencyRepository.fetchCurrencies() }
            val currencies = result.getOrNull()

            val state = if (currencies == null) {
                CurrenciesState.Error
            } else {
                CurrenciesState.Data(currencies)
            }
            _viewState.tryEmit(viewState.value.copy(currenciesState = state))
        }
    }

    data class ViewState(
        val currenciesState: CurrenciesState
    ) {
        sealed class CurrenciesState {
            object Loading : CurrenciesState()
            object Error : CurrenciesState()
            data class Data(val currencies: Map<String, String>) : CurrenciesState()
        }
    }

    sealed class UiAction {
        object CloseScreen : UiAction()
    }
}
