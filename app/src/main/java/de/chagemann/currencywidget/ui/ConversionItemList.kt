package de.chagemann.currencywidget.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.chagemann.currencywidget.MainViewModel

@Composable
fun ConversionItemList(
    modifier: Modifier = Modifier,
    data: List<ConversionItemData>,
    contentPadding: PaddingValues,
    onAction: (MainViewModel.UiAction) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        items(
            count = data.size,
            key = { index -> data[index].itemUuid },
            contentType = { "ConversionItem" }
        ) { index ->
            ConversionItem(
                conversionItemData = data[index],
                onAction = onAction
            )
        }

        if (data.isEmpty()) {
            item(contentType = "AddFirstConversionItem") {
                AddFirstConversionItem(onAction = onAction)
            }
        }
        when (data.size) {
            in 0..2 -> {
                item(contentType = "AddFurtherConversionItem") {
                    AddFurtherConversionItem(onAction = onAction)
                }
            }
        }
    }
}
