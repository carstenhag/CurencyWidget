package de.chagemann.currencywidget.ui.currencyselection

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.chagemann.currencywidget.DispatcherProvider
import de.chagemann.currencywidget.MainViewModel
import de.chagemann.currencywidget.R
import de.chagemann.currencywidget.data.CurrencyRepository
import de.chagemann.currencywidget.data.ICurrencyApi
import de.chagemann.currencywidget.data.PricePairsDto
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencySelectionModal(
    parentOnAction: (MainViewModel.UiAction) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CurrencySelectionViewModel = viewModel(),
    isVisible: Boolean,
) {
    val state = viewModel.viewState.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = isVisible) {
        if (isVisible) {
            scope.launch { sheetState.show() }
        } else {
            scope.launch { sheetState.hide() }
        }
    }

    if (!isVisible) return

    BackHandler(enabled = isVisible) {
        Log.d("Carsten", "BackHandler")
        parentOnAction(MainViewModel.UiAction.HidePicker)
    }
    LaunchedEffect(key1 = "123") {
        viewModel.loadCurrencies()
    }

    ModalBottomSheet(
        onDismissRequest = {
            Log.d("Carsten", "onDismissRequest")
            parentOnAction(MainViewModel.UiAction.HidePicker)
        },
        modifier = modifier.fillMaxSize(),
        sheetState = sheetState
    ) {
        TopAppBar(title = { Text(text = "Select Currencies") },
            actions = {
                IconButton(onClick = {
                    //viewModel.onAction(CurrencySelectionViewModel.UiAction.CloseScreen)
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            Log.d("Carsten", "!sheetState.isVisible")
                            parentOnAction(MainViewModel.UiAction.HidePicker)
                        }
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.surfaceTint
                    )
                }
            })

        when (val currenciesState = state.value.currenciesState) {
            is CurrencySelectionViewModel.ViewState.CurrenciesState.Data -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    val currencyItems =
                        currenciesState.currencies.toList()

                    items(
                        count = currencyItems.size,
                        contentType = { "CurrencyGridItem" }
                    ) { index ->
                        CurrencyGridItem(
                            currencyCode = currencyItems[index].first,
                            currencyName = currencyItems[index].second
                        )
                    }
                }
            }

            is CurrencySelectionViewModel.ViewState.CurrenciesState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is CurrencySelectionViewModel.ViewState.CurrenciesState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Card(
                        onClick = {
                            viewModel.loadCurrencies()
                        },
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_retry),
                                contentDescription = "Reload",
                                tint = MaterialTheme.colorScheme.surfaceTint
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "There was an error. Try again?",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CurrencyGridItem(
    modifier: Modifier = Modifier,
    currencyCode: String,
    currencyName: String
) {
    Card {
        Column(modifier = modifier.padding(vertical = 4.dp, horizontal = 2.dp)) {
            Text(
                text = currencyCode,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = currencyName,
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 2,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview
@Composable
fun CurrencyGridItemPreview() {
    MaterialTheme {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { CurrencyGridItem(currencyCode = "EUR", currencyName = "Euro") }
            item { CurrencyGridItem(currencyCode = "USD", currencyName = "US Dollars") }
            item { CurrencyGridItem(currencyCode = "CHF", currencyName = "Swiss Francs") }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencySelectionModalPreview() {
    MaterialTheme {
        CurrencySelectionModal(
            parentOnAction = {},
            viewModel = CurrencySelectionViewModel(
                CurrencyRepository(currencyApi = object :
                    ICurrencyApi {
                    override suspend fun getCurrencyList(): Map<String, String> {
                        return mapOf("EUR" to "Euro")
                    }

                    override suspend fun getPricePairsForBaseCurrency(baseCurrency: String): PricePairsDto {
                        return PricePairsDto("", mapOf())
                    }

                }
                ), dispatcherProvider = DispatcherProvider()
            ),
            isVisible = true
        )
    }
}
