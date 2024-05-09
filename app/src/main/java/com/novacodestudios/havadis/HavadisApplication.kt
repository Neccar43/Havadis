package com.novacodestudios.havadis

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.novacodestudios.havadis.domain.preferences.UserPreferences
import com.novacodestudios.havadis.worker.NotificationWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import androidx.work.WorkInfo.State

@HiltAndroidApp
class HavadisApplication : Application() {
    companion object {
        private const val TAG = "Application"

        private var applicationScope = MainScope()
    }

    @Inject
    lateinit var preferences: UserPreferences
    private var isWorkManagerSetup = false

    override fun onCreate() {
        super.onCreate()
        if (!isWorkManagerSetup) {
            setupWorkManager()
            isWorkManagerSetup = true
        }

    }

    override fun onLowMemory() {
        super.onLowMemory()
        applicationScope.cancel("onLowMemory() called by system")
        applicationScope = MainScope()
    }

    private fun setupWorkManager() {
        applicationScope.launch {
            preferences.getShowNotification.collectLatest { isShowNotificationEnable ->
                Log.d(TAG, "setupWorkManager: isShowNotificationEnable:$isShowNotificationEnable")
                if (isShowNotificationEnable) {
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                    val request =
                        PeriodicWorkRequestBuilder<NotificationWorker>(10, TimeUnit.MINUTES)
                            .setInitialDelay(10, TimeUnit.MINUTES)
                            .setConstraints(constraints)
                            .build()

                    WorkManager.getInstance(this@HavadisApplication).enqueue(request)
                    return@collectLatest
                }
            }
        }
    }

}