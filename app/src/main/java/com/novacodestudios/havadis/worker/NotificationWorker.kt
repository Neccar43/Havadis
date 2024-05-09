package com.novacodestudios.havadis.worker

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.novacodestudios.havadis.R
import com.novacodestudios.havadis.data.remote.dto.Article
import com.novacodestudios.havadis.domain.repository.NewsRepository
import com.novacodestudios.havadis.presentation.MainActivity
import com.novacodestudios.havadis.util.Country
import com.novacodestudios.havadis.util.NotiKitBuilder
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: NewsRepository,
) : CoroutineWorker(appContext, workerParams) {
    companion object{
        private const val TAG="NotificationWorker"
    }
    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "doWork: success")
            sendNotification()

            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "doWork: fail $e")
            Result.failure()
        }
    }

    // TODO: Bildirimleri özelleştir
    private fun sendNotification(){
        Log.d(TAG, "sendNotification: ")
        NotiKitBuilder(applicationContext)
            .setChannel("news_id", "News", "For news")
            .setContent(
                applicationContext.getString(R.string.notification_title),
                applicationContext.getString(R.string.notification_content)
            )
            .setIntent(Intent(applicationContext, MainActivity::class.java))
            .setSmallIcon(R.drawable.language_fill0_wght400_grad0_opsz24)
            .build()
    }


}