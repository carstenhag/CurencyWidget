package de.chagemann.currencywidget

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.chagemann.currencywidget.data.UserDataEntity
import de.chagemann.currencywidget.data.UserDataXXXEntitySerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

private const val USER_DATA = "user_data"

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    fun provideDataStore(@ApplicationContext appContext: Context): DataStore<UserDataEntity> {
        return DataStoreFactory.create(
            serializer = UserDataXXXEntitySerializer,
            produceFile = { appContext.dataStoreFile(USER_DATA) },
            corruptionHandler = null,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }
}
