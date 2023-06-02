package de.chagemann.currencywidget.ui.currencyselection

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.chagemann.currencywidget.R

@Composable
fun CurrencySelectionModal(
    modifier: Modifier = Modifier,
    viewModel: CurrencySelectionViewModel = viewModel(),
    isVisible: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
) {
    BackHandler(
        enabled = isVisible.value
    ) {
        isVisible.value = false
    }

    LaunchedEffect(key1 = "123") {
        viewModel.loadCurrencies()
    }

    val state = viewModel.viewState.collectAsState()

    IconButton(onClick = { viewModel.onAction(CurrencySelectionViewModel.UiAction.CloseScreen) }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = "Close",
            tint = MaterialTheme.colorScheme.surfaceTint
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (state.value.currenciesState is CurrencySelectionViewModel.ViewState.CurrenciesState.Data) {
            val currencyItems =
                (state.value.currenciesState as CurrencySelectionViewModel.ViewState.CurrenciesState.Data).currencies.toList()

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

@Preview
@Composable
fun CurrencySelectionModalPreview() {
    MaterialTheme {
        CurrencySelectionModal()
    }
}
