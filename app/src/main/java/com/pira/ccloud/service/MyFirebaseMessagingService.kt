package com.pira.ccloud.service

import android.app.NotificationManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pira.ccloud.utils.AppNotificationManager
import com.pira.ccloud.utils.AppNotificationManager.createCustomNotification

class MyFirebaseMessagingService : FirebaseMessagingService(){

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        AppNotificationManager.createNotificationChannel(
            this ,
            AppNotificationManager.GENERAL_NOTIFICATION_CHANNEL ,
            AppNotificationManager.GENERAL_NOTIFICATION_CHANNEL_NAME ,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val title = message.notification?.title
        val description = message.notification?.body
        val url = message.data["url"]

        createCustomNotification(
            context = this ,
            notificationChannel = AppNotificationManager.GENERAL_NOTIFICATION_CHANNEL ,
            notificationId = (0..1000).random() ,
            title = title ?: "" ,
            description = description ?: "" ,
            url = url
        )
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

}