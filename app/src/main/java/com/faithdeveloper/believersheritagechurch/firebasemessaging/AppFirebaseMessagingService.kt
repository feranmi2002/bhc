package com.faithdeveloper.believersheritagechurch.firebasemessaging

import android.annotation.SuppressLint
import android.content.Intent
import com.faithdeveloper.believersheritagechurch.utils.NotificationUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class AppFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        message.notification?.let { notification: RemoteMessage.Notification ->
            NotificationUtil.showUpcomingProgrammeNotification(
                notification.title, notification.body, notification.imageUrl,
                this
            )

        }
        super.onMessageReceived(message)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}