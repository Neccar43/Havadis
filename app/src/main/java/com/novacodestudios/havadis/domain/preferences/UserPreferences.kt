package com.novacodestudios.havadis.domain.preferences

import kotlinx.coroutines.flow.Flow


interface UserPreferences {
    val getDarkMode: Flow<Boolean>
    suspend fun setDarkMode(darkMode:Boolean)

    val getLanguage: Flow<String>
    suspend fun setLanguage(language:String)

    val getImageWifiOnly: Flow<Boolean>
    suspend fun setImageWifiOnly(imageWifiOnly:Boolean)

    val getShowNotification: Flow<Boolean>
    suspend fun setShowNotification(showNotification:Boolean)
}