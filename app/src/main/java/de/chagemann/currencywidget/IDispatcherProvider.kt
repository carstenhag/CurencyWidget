package de.chagemann.currencywidget

import kotlinx.coroutines.CoroutineDispatcher

@Suppress("PropertyName")
interface IDispatcherProvider {
    val IO: CoroutineDispatcher
    val Unconfined: CoroutineDispatcher
    val Default: CoroutineDispatcher
    val Main: CoroutineDispatcher
}
