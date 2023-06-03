package de.chagemann.currencywidget

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.chagemann.currencywidget.data.ConversionItemData
import de.chagemann.currencywidget.data.ICurrencyRepository
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

    val userDataFlow = currencyRepository.userData

    private val _viewState = MutableStateFlow(
        ViewState(
            conversionItemDataList = listOf(),
            showDeletionDialogForItem = null,
            pickerState = ViewState.PickerState.PickerClosed
        )
    )
    val viewState = _viewState.asStateFlow()

    fun loadDataForNewBaseCurrency(baseCurrency: String) {
        val lowercaseBaseCurrency = baseCurrency.lowercase()

        coroutineScope.launch {
            val pricePairs = currencyRepository.fetchPricePairs(lowercaseBaseCurrency)
            if (pricePairs != null) {
            }
        }
    }

    fun onAction(action: UiAction) {
        when (action) {
            is UiAction.OpenPickerForNewItem -> {
                coroutineScope.launch {
                    currencyRepository.addConversionItem("EUR", 10.0, "USD")
                }

                /*
                _viewState.tryEmit(
                   viewState.value.copy(pickerState = ViewState.PickerState.PickerOpenForNewItem)
                )

                 */
            }
            is UiAction.OpenPicker -> _viewState.tryEmit(
                viewState.value.copy(
                    pickerState = ViewState.PickerState.PickerOpenForExistingItem(
                        conversionItemData = action.conversionItemData,
                        origin = action.origin
                    )
                )
            )
            is UiAction.SwapCurrency -> TODO() // swap values, persist
            is UiAction.ShowDeletionDialog -> _viewState.tryEmit(
                viewState.value.copy(showDeletionDialogForItem = action.conversionItemData)
            )
            is UiAction.DeleteItem -> TODO()
            is UiAction.HideDeletionDialog -> _viewState.tryEmit(
                viewState.value.copy(showDeletionDialogForItem = null)
            )
            is UiAction.HidePicker -> _viewState.tryEmit(
                viewState.value.copy(pickerState = ViewState.PickerState.PickerClosed)
            )
        }
    }

    data class ViewState constructor(
        val conversionItemDataList: List<ConversionItemData>,
        val showDeletionDialogForItem: ConversionItemData? = null,
        val pickerState: PickerState,
    ) {
        sealed class PickerState {
            object PickerClosed : PickerState()

            data class PickerOpenForExistingItem(
                val conversionItemData: ConversionItemData,
                val origin: UiAction.OpenPicker.Origin
            ) : PickerState()

            object PickerOpenForNewItem : PickerState()
        }
    }

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

        object HidePicker : UiAction()

        data class ShowDeletionDialog(val conversionItemData: ConversionItemData) : UiAction()
        object HideDeletionDialog : UiAction()

        object DeleteItem : UiAction()

        object OpenPickerForNewItem : UiAction()
    }

}
