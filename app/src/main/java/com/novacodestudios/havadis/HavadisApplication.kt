package com.novacodestudios.havadis

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.Observer
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
class HavadisApplication:Application(), Configuration.Provider {
    companion object{
        private const val TAG="Application"

        private var applicationScope= MainScope()
    }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var preferences: UserPreferences
    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        val workManager=WorkManager.getInstance(this)
        val constraints=Constraints.Builder()
           // .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val notificationRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            repeatInterval = 15, // Tekrarlama aralığı dakika cinsinden
            repeatIntervalTimeUnit = TimeUnit.MINUTES // Tekrarlama aralığı birimi
        ).build()

        workManager.enqueue(notificationRequest)

        workManager.getWorkInfoByIdLiveData(notificationRequest.id).observeForever { workInfo ->
            if (workInfo != null) {
                Log.d(TAG, "worker state:${workInfo.state}")

            }
        }


        applicationScope.launch {
            preferences.getShowNotification.collectLatest {isShowNotificationEnable->
                Log.d(TAG, "getShowNotification: $isShowNotificationEnable")
            }
        }

    }

    override fun onLowMemory() {
        super.onLowMemory()
        applicationScope.cancel("onLowMemory() called by system")
        applicationScope= MainScope()
    }

}