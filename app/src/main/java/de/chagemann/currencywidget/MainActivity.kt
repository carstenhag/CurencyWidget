package de.chagemann.currencywidget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import dagger.hilt.android.AndroidEntryPoint
import de.chagemann.currencywidget.ui.theme.CurrencyWidgetTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = viewModel.viewState.collectAsState()
            CurrencyWidgetTheme {
                LaunchedEffect(key1 = "") {
                    viewModel.loadDataForNewBaseCurrency("EUR")
                }

                CurrencyScreen(
                    state = state,
                    onAction = { action -> viewModel.onAction(action) }
                )
            }
        }
    }
}
