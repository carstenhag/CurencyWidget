package de.chagemann.currencywidget.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object UserDataXXXEntitySerializer : Serializer<UserDataEntity> {
    override val defaultValue: UserDataEntity
        get() = UserDataEntity.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserDataEntity {
        try {
            return UserDataEntity.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserDataEntity, output: OutputStream) {
        t.writeTo(output)
    }

}
