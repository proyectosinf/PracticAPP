package com.mobivery.fct25.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mobivery.fct25.R
import com.mobivery.fct25.RootActivity
import com.mobivery.fct25.data.network.SessionProvider
import com.mobivery.fct25.domain.repository.AuthRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationsService : FirebaseMessagingService() {

    companion object {
        private const val CHANNEL_ID = "custom_channel_id"
        private const val TITLE = "title"
        private const val MESSAGE = "message"
        private const val SUBJECT = "subject"
        private const val ID = "id"
    }

    @Inject
    lateinit var sessionProvider: SessionProvider

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        registerDevice(token)
    }

    private fun registerDevice(token: String) {}

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        var message: String? = null
        var title: String? = null
        var id = 1
        var type: NotificationType? = null

        remoteMessage.data.let {
            if (it.isNotEmpty()) {
                title = it[TITLE]
                message = it[MESSAGE]
                type = getNotificationType(it[SUBJECT])
                id = it[ID]?.toInt() ?: 1
            }
        }

        remoteMessage.notification?.let {
            message = it.body
            title = it.title
        }

        sendNotification(id, title, message, type)
    }

    private fun sendNotification(
        notificationId: Int,
        title: String?,
        message: String?,
        type: NotificationType?,
    ) {
        val args = Bundle()

        when (type) {
            NotificationType.NOTIFICATION_TYPE_1 -> {
                args.putString("notificationType", NotificationType.NOTIFICATION_TYPE_1.code)
            }
            NotificationType.NOTIFICATION_TYPE_2 -> {
                args.putString("notificationType", NotificationType.NOTIFICATION_TYPE_2.code)
            }
            else -> {
                /* no args */
            }
        }

        val openAppIntent = Intent(this, RootActivity::class.java)
        val openAppPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(openAppIntent)
            getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setSmallIcon(R.drawable.logo_icon)
            .setColor(ContextCompat.getColor(this, R.color.primary_color))
            .setContentIntent(openAppPendingIntent)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(message))

        val notificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_ID,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}