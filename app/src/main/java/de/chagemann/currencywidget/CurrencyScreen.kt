package de.chagemann.currencywidget

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import de.chagemann.currencywidget.ui.ConversionItemData
import de.chagemann.currencywidget.ui.ConversionItemList
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyScreen(
    state: State<MainViewModel.ViewState>,
    onAction: (MainViewModel.UiAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                actions = {
                    IconButton(onClick = { onAction(MainViewModel.UiAction.AddNewThingy) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "Add",
                            tint = MaterialTheme.colorScheme.surfaceTint
                        )
                    }
                })
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ConversionItemList(
                data = state.value.conversionItemDataList,
                onAction = onAction
            )
        }
    }
}

class ConversionItemDataListProvider : PreviewParameterProvider<List<ConversionItemData>> {
    private val plnToEur = ConversionItemData(
        itemUuid = UUID.randomUUID().toString(),
        baseCurrencyCode = "PLN",
        baseCurrencyAmount = 45.0,
        targetCurrencyCode = "EUR",
        exchangeRate = 0.22
    )
    private val eurToUsd = ConversionItemData(
        itemUuid = UUID.randomUUID().toString(),
        baseCurrencyCode = "EUR",
        baseCurrencyAmount = 23.4,
        targetCurrencyCode = "USD",
        exchangeRate = 1.21
    )

    override val values: Sequence<List<ConversionItemData>>
        get() = sequenceOf(
            listOf(),
            listOf(plnToEur),
            listOf(
                plnToEur,
                plnToEur.copy(itemUuid = UUID.randomUUID().toString()).swapCurrencies()
            ),
            listOf(
                plnToEur,
                plnToEur.copy(itemUuid = UUID.randomUUID().toString()).swapCurrencies(),
                eurToUsd
            ),
        )

}

@Preview
@Composable
fun CurrencyScreenPreview(
    @PreviewParameter(ConversionItemDataListProvider::class) data: List<ConversionItemData>
) {
    val state = MutableStateFlow(
        MainViewModel.ViewState(
            currencies = mapOf(
            ),
            pricePairs = null,
            baseCurrency = "EUR",
            conversionItemDataList = data
        )
    ).collectAsState()

    MaterialTheme {
        CurrencyScreen(state = state, onAction = {})
    }
}
