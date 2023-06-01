package de.chagemann.currencywidget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LaunchedEffect(key1 = "") {
                        viewModel.loadDataForNewBaseCurrency("EUR")
                    }

                    Column {
                        Text(text = "Base currency: ${state.value.baseCurrency}")
                        Spacer(modifier = Modifier.height(24.dp))

                        val pricePairs = state.value.pricePairs ?: return@Surface
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.secondary)
                        ) {
                            items(pricePairs.pairs) { pricePair ->
                                CurrencyPair(
                                    targetCurrency = pricePair.targetCurrency,
                                    value = pricePair.value.toString()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CurrencyPair(targetCurrency: String, value: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 2.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(text = targetCurrency)
        Text(text = value)
    }
}
