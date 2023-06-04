package de.chagemann.currencywidget.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.chagemann.currencywidget.MainViewModel
import de.chagemann.currencywidget.R
import de.chagemann.currencywidget.data.ConversionItemData
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversionItem(
    conversionItemData: ConversionItemData,
    modifier: Modifier = Modifier,
    onAction: (MainViewModel.UiAction) -> Unit,
) {
    Card(
        onClick = {},
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .pointerInput(conversionItemData) {
                    detectTapGestures(
                        onLongPress = {
                            onAction(MainViewModel.UiAction.ShowDeletionDialog(conversionItemData))
                        }
                    )
                }
                .padding(24.dp),
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = conversionItemData.formattedBaseCurrencyAmount,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 24.sp,
                    )
                    Text(
                        text = conversionItemData.baseCurrencyCode,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                IconButton(
                    onClick = { onAction(MainViewModel.UiAction.SwapCurrency(conversionItemData)) },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_swap),
                        contentDescription = "Swap",
                        tint = MaterialTheme.colorScheme.surfaceTint
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = conversionItemData.formattedTargetCurrencyAmount,
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 24.sp,
                    )
                    Text(
                        text = conversionItemData.targetCurrencyCode,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = conversionItemData.itemSubline,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ConversionItemPreview(
    @PreviewParameter(ConversionItemDataProvider::class) data: ConversionItemData,
) {
    MaterialTheme {
        ConversionItem(
            conversionItemData = data,
            modifier = Modifier.padding(16.dp),
            onAction = {}
        )
    }
}

class ConversionItemDataProvider : PreviewParameterProvider<ConversionItemData> {
    private val plnToEur = ConversionItemData(
        itemUuid = UUID.randomUUID().toString(),
        baseCurrencyCode = "PLN",
        baseCurrencyAmount = 45.0,
        formattedBaseCurrencyAmount = "123",
        targetCurrencyCode = "EUR",
        exchangeRate = 0.22,
        targetCurrencyAmount = 123.0,
        formattedTargetCurrencyAmount = "123",
    )
    override val values: Sequence<ConversionItemData>
        get() = sequenceOf(
            plnToEur,
            plnToEur.copy(itemUuid = UUID.randomUUID().toString()).copyAndSwapCurrencies(),
        )
}


