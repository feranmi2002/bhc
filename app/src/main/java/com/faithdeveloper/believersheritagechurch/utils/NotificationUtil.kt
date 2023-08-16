package com.faithdeveloper.believersheritagechurch.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.faithdeveloper.believersheritagechurch.R
import com.faithdeveloper.believersheritagechurch.data.announcement.Announcement
import com.faithdeveloper.believersheritagechurch.ui.MainActivity
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.*

object NotificationUtil {

    val PLAYING_MESSAGE_NOTIFICATION_CHANNEL_ID = "Playing Message"
    val NEW_PROGRAM_NOTIFICATION_CHANNEL_ID = "Upcoming Program"


    fun Context.createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = PLAYING_MESSAGE_NOTIFICATION_CHANNEL_ID
            val descriptionText = "Allows you to control a playing message"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                PLAYING_MESSAGE_NOTIFICATION_CHANNEL_ID,
                name,
                importance
            ).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            createUpcomingProgramNotificationChannel()
        }
    }

    private fun Context.createUpcomingProgramNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = NEW_PROGRAM_NOTIFICATION_CHANNEL_ID
            val descriptionText = "Reminds you about upcoming programmes"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                NEW_PROGRAM_NOTIFICATION_CHANNEL_ID,
                name,
                importance
            ).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showUpcomingProgrammeNotification(
        title: String?,
        body: String?,
        imageLink: Uri?,
        context: Context
    ) {

        val job: Job = SupervisorJob()
        val scope = CoroutineScope(kotlinx.coroutines.Dispatchers.IO + job)

        val notificationBuilder: NotificationCompat.Builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationCompat.Builder(
                    context, NEW_PROGRAM_NOTIFICATION_CHANNEL_ID
                )
            } else {
                NotificationCompat.Builder(context)
            }

        notificationBuilder.setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setChannelId(NEW_PROGRAM_NOTIFICATION_CHANNEL_ID)


        if (imageLink != null) {
            scope.launch {
                Glide.with(context)
                    .asBitmap()
                    .addListener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            notificationBuilder.setStyle(
                                NotificationCompat.BigTextStyle().bigText(body)
                            )
                            with(NotificationManagerCompat.from(context)) {
                                notify(
                                    200,
                                    notificationBuilder.build()
                                )
                            }
                            return false
                        }

                        override fun onResourceReady(
                            resource: Bitmap?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            if (resource != null) {
                                notificationBuilder.setStyle(
                                    NotificationCompat.BigPictureStyle().bigPicture(resource)
                                )
                                with(NotificationManagerCompat.from(context)) {
                                    notify(
                                        200,
                                        notificationBuilder.build()
                                    )
                                }
                            }

                            return false
                        }

                    })
            }
        } else {
            notificationBuilder.setStyle(
                NotificationCompat.BigTextStyle().bigText(body)
            )
            with(NotificationManagerCompat.from(context)) {
                notify(200, notificationBuilder.build())
            }
        }

    }
}