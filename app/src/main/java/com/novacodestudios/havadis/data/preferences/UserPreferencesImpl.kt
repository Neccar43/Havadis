package com.novacodestudios.havadis.data.preferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.novacodestudios.havadis.domain.preferences.UserPreferences
import com.novacodestudios.havadis.util.Country
import com.novacodestudios.havadis.util.Language
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Top-level de tanımlanmalı
val Context.dataStore:DataStore<Preferences> by preferencesDataStore(name= UserPreferencesImpl.DATA_STORE_NAME)

class UserPreferencesImpl(
    private val context: Context
) : UserPreferences {
    companion object{
        const val DATA_STORE_NAME="settings"
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        private val LANGUAGE_KEY = stringPreferencesKey("language")
        private val COUNTRY_KEY = stringPreferencesKey("country")
        private val IMAGES_WIFI_ONLY_KEY = booleanPreferencesKey("images_wifi_only")
        private val NOTIFICATION_KEY= booleanPreferencesKey("show_notification")
        private const val TAG="UserPreferences"
    }

    override val getDarkMode: Flow<Boolean>
        get() = context.dataStore.data.map {preferences->
            preferences[DARK_MODE_KEY] ?: false
        }

    override suspend fun setDarkMode(darkMode: Boolean) {
        context.dataStore.edit {preferences->
            Log.d(TAG, "setDarkMode: $preferences")
            preferences[DARK_MODE_KEY]=darkMode
        }
    }

    override val getLanguage: Flow<Language?>
        get() = context.dataStore.data.map {preferences->
            val languageCode=preferences[LANGUAGE_KEY]
            languageCode?.run { Language.fromLanguageCode(this) }
        }

    override suspend fun setLanguage(language: Language) {
        context.dataStore.edit {preferences->
            preferences[LANGUAGE_KEY]=language.code
        }
    }

    override val getCountry: Flow<Country?>
        get() = context.dataStore.data.map {preferences->
            val countryCode=preferences[COUNTRY_KEY]
            countryCode?.run { Country.fromCountryCode(this) }
        }

    override suspend fun setCountry(country: Country) {
        context.dataStore.edit {preferences->
            preferences[COUNTRY_KEY]=country.countryCode
        }
    }

    override val getImageWifiOnly: Flow<Boolean>
        get() = context.dataStore.data.map {preferences->
            preferences[IMAGES_WIFI_ONLY_KEY] ?: false
        }

    override suspend fun setImageWifiOnly(imageWifiOnly: Boolean) {
        context.dataStore.edit {preferences->
            Log.d(TAG, "setImageWifiOnly: $imageWifiOnly")
            preferences[IMAGES_WIFI_ONLY_KEY]=imageWifiOnly
        }
    }

    override val getShowNotification: Flow<Boolean>
        get() = context.dataStore.data.map {preferences->
            preferences[NOTIFICATION_KEY]?:true
        }

    override suspend fun setShowNotification(showNotification: Boolean) {
        context.dataStore.edit {preferences->
            preferences[NOTIFICATION_KEY]=showNotification
        }
    }


}