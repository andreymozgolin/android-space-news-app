package ru.andreymozgolin.spacenews.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import ru.andreymozgolin.spacenews.MainActivity
import ru.andreymozgolin.spacenews.R
import ru.andreymozgolin.spacenews.data.Article
import javax.inject.Inject
import javax.inject.Singleton

const val TAG = "NotificationUtils"

@Singleton
class Notifications @Inject constructor(context: Context?) : ContextWrapper(context) {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        Log.d(TAG, "Creating notification channel")
        val channel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.notification_channel),
            NotificationManager.IMPORTANCE_LOW
        )

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun createNotification(
        title: String? = null,
        text: String? = null,
        intent: PendingIntent? = null
    ): NotificationCompat.Builder {

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(intent)
    }

    fun send(id: Int, notification: NotificationCompat.Builder) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id, notification.build())
    }

    fun lastNewsNotification(articles: List<Article>) {
        if (articles.isEmpty()) return

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = createNotification(
            title = "There are ${articles.size} new space news",
            intent = pendingIntent
        )
        send(LAST_NEWS_NOTIFICATION_ID, notification)
    }

    companion object {
        const val CHANNEL_ID = "ru.andreymozgolin.spacenews.CHANNEL"
        const val LAST_NEWS_FOREGROUND_ID = 1
        const val LAST_NEWS_NOTIFICATION_ID = 2
    }
}