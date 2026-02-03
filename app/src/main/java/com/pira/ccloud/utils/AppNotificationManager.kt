package com.pira.ccloud.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.pira.ccloud.MainActivity
import com.pira.ccloud.R
import androidx.core.net.toUri

object AppNotificationManager {

    const val GENERAL_NOTIFICATION_CHANNEL = "GeneralNotificationChannel"
    const val GENERAL_NOTIFICATION_CHANNEL_NAME = "General notification"
    const val GENERAL_NOTIFICATION_NOTIFICATION_ID = 3


    fun createNotificationChannel(context: Context ,channel : String ,name : String , importance : Int) {
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(
            NotificationChannel(
                channel,
                name,
                importance
            )
        )
    }

    fun createCustomNotification(context: Context , notificationChannel : String , notificationId : Int , title : String , description : String , url : String ?= null) {

        val intent = if (url == null) {
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        } else {
             Intent(Intent.ACTION_VIEW, url.toUri()).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        }

        val notification = NotificationCompat.Builder(context, notificationChannel)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            )
            .setAutoCancel(true)


        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification.build())
    }

}