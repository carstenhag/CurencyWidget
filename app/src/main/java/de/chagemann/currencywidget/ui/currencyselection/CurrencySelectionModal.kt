package de.chagemann.currencywidget.ui.currencyselection

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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

    IconButton(onClick = { viewModel.onAction(CurrencySelectionViewModel.UiAction.CloseScreen) }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = "Close",
            tint = MaterialTheme.colorScheme.surfaceTint
        )
    }
}

@Preview
@Composable
fun CurrencySelectionModal() {
    MaterialTheme {
        CurrencySelectionModal()
    }
}
