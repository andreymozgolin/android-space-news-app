package ru.andreymozgolin.spacenews.services

import android.app.*
import android.content.Intent
import android.os.IBinder
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.andreymozgolin.spacenews.R
import ru.andreymozgolin.spacenews.SpaceNewsApplication
import ru.andreymozgolin.spacenews.data.ArticleRepository
import ru.andreymozgolin.spacenews.utils.Notifications
import javax.inject.Inject

private const val TAG = "NotificationService"

class NotificationService: Service() {
    @Inject lateinit var repository: ArticleRepository
    @Inject lateinit var notifications: Notifications

    override fun onCreate() {
        (application as SpaceNewsApplication).component.inject(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = notifications.createNotification(getString(R.string.foreground_notification))
        startForeground(Notifications.LAST_NEWS_FOREGROUND_ID, notification.build())

        repository.getLastArticles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                notifications.lastNewsNotification(it)
            },{
                Log.d(TAG, "Couldn't check last news: ${it.message}", it)
                stopSelf(startId)
            },{
                stopSelf(startId)
            })

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}