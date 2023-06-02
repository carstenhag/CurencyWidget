package de.chagemann.currencywidget.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import de.chagemann.currencywidget.MainViewModel

@Composable
fun ConversionItemList(
    data: List<ConversionItemData>,
    onAction: (MainViewModel.UiAction) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 24.dp),
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
