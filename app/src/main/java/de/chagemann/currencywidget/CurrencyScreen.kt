package de.chagemann.currencywidget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import de.chagemann.currencywidget.ui.ConversionItemData
import de.chagemann.currencywidget.ui.ConversionItemList
import de.chagemann.currencywidget.ui.currencyselection.CurrencySelectionModal
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
            Column {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.7f)),
                )
                ConversionItemList(
                    data = state.value.conversionItemDataList,
                    contentPadding = PaddingValues(24.dp),
                    onAction = onAction
                )
            }
        }

        if (state.value.showDeletionDialogForItem != null) {
            AlertDialog(
                onDismissRequest = {},
                confirmButton = {
                    TextButton(onClick = { onAction(MainViewModel.UiAction.DeleteItem) }) {
                        Text(text = "Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { onAction(MainViewModel.UiAction.HideDeletionDialog) }) {
                        Text(text = "Dismiss")
                    }
                },
                title = { Text(text = "Delete conversion item") },
                text = { Text(text = "Do you want to delete this conversion item? You can just re-add it when needed.") },
            )
        }

        if (state.value.showDialog) {
            CurrencySelectionModal()
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
            conversionItemDataList = data
        )
    ).collectAsState()

    MaterialTheme {
        CurrencyScreen(state = state, onAction = {})
    }
}

@Preview
@Composable
fun CurrencyScreenDeletionDialogPreview() {
    val plnToEur = ConversionItemData(
        itemUuid = UUID.randomUUID().toString(),
        baseCurrencyCode = "PLN",
        baseCurrencyAmount = 45.0,
        targetCurrencyCode = "EUR",
        exchangeRate = 0.22
    )
    val state = MutableStateFlow(
        MainViewModel.ViewState(
            conversionItemDataList = listOf(),
            showDeletionDialogForItem = plnToEur
        )
    ).collectAsState()

    MaterialTheme {
        CurrencyScreen(state = state, onAction = {})
    }
}
