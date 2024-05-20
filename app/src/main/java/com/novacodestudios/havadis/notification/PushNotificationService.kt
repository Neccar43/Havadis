package com.novacodestudios.havadis.notification

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.novacodestudios.havadis.R
import com.novacodestudios.havadis.presentation.MainActivity
import com.novacodestudios.havadis.util.NotiKitBuilder
import com.novacodestudios.havadis.worker.NotificationWorker

// TODO: Bu paketi başka bir yere taşı
class PushNotificationService : FirebaseMessagingService() { //manifest te servisimizi tanımladık
    companion object {
        private const val TAG = "PushNotificationService"
    }

    //yeni tokeni sunuya gönder
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")

    }

    // bildirim istemciye ulaşytığında çalışr
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (message.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + message.data)
        }
        message.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }


        message.notification?.let {
            Log.d(TAG, "sendNotification: title:${it.title} ")
            Log.d(TAG, "sendNotification: body:${it.body} ")

            // TODO: elvisleri kaldır
            NotiKitBuilder(applicationContext)
                .setChannel("news_id", "News", "For news")
                .setContent(title = it.title?:"title null", text = it.body?:"body null")
                .setIntent(Intent(applicationContext, MainActivity::class.java))
                .setSmallIcon(R.drawable.language_fill0_wght400_grad0_opsz24)
                .build()
        }



    }
}