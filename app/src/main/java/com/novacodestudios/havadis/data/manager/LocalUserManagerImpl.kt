package com.novacodestudios.havadis.data.manager

import android.content.Context
import com.novacodestudios.havadis.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.Flow

// TODO: Bu classı birden fazla data store için kullanabilir miyiz yoksa her biri için ayrı bir class mı oluşturmalıyız
class LocalUserManagerImpl(
    private val context: Context
) : LocalUserManager {
    override suspend fun saveAppEntry() {
        TODO("Not yet implemented")
    }

    override fun readAppEntry(): Flow<Boolean> {
        TODO("Not yet implemented")
    }
}
// TODO: Context ten bir extension oluştur ve yukarıdaki class da kullan