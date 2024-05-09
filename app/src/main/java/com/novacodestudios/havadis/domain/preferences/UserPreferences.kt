package com.novacodestudios.havadis.domain.preferences

import com.novacodestudios.havadis.util.Country
import com.novacodestudios.havadis.util.Language
import kotlinx.coroutines.flow.Flow


interface UserPreferences {
    val getDarkMode: Flow<Boolean>
    suspend fun setDarkMode(darkMode:Boolean)

    val getLanguage: Flow<Language?>
    suspend fun setLanguage(language:Language)

    val getImageWifiOnly: Flow<Boolean>
    suspend fun setImageWifiOnly(imageWifiOnly:Boolean)

    val getShowNotification: Flow<Boolean>
    suspend fun setShowNotification(showNotification:Boolean)

    val getCountry:Flow<Country?>
    suspend fun setCountry(country: Country)

}