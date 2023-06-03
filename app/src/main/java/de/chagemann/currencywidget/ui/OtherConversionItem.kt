package de.chagemann.currencywidget.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.chagemann.currencywidget.MainViewModel
import de.chagemann.currencywidget.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFirstConversionItem(
    modifier: Modifier = Modifier,
    onAction: (MainViewModel.UiAction) -> Unit,
) {
    Card(
        onClick = {
            onAction(MainViewModel.UiAction.OpenPickerForNewItem)
        },
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "Add",
                tint = MaterialTheme.colorScheme.surfaceTint
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Add your first item now :)",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
fun AddFirstConversionItemPreview() {
    MaterialTheme {
        AddFirstConversionItem(onAction = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFurtherConversionItem(
    modifier: Modifier = Modifier,
    onAction: (MainViewModel.UiAction) -> Unit,
) {
    Card(
        onClick = {
            onAction(MainViewModel.UiAction.OpenPickerForNewItem)
        },
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Tip: You can add multiple conversions with the same currencies, to have different amount conversions shown in an overview.",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
fun AddFurtherConversionItemPreview() {
    MaterialTheme {
        AddFurtherConversionItem(onAction = {})
    }
}

