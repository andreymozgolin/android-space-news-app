package ru.andreymozgolin.spacenews.receivers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.util.Log
import ru.andreymozgolin.spacenews.services.NotificationService

private const val TAG = "AlarmReceiver"

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "Starting NotificationService from AlarmReceiver...")
        val intent = Intent(context, NotificationService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context?.startForegroundService(intent)
        } else {
            context?.startService(intent)
        }
    }

    companion object {
        fun setAlarm(context: Context?) {
            Log.d(TAG, "Setup alarm")
            val pendingIntent = PendingIntent.getBroadcast(context, 0,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            (context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager).setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                pendingIntent
            )
        }
    }
}