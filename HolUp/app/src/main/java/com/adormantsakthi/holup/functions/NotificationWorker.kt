package com.adormantsakthi.holup.functions

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.adormantsakthi.holup.MainActivity
import com.adormantsakthi.holup.R
import java.util.Calendar
import java.util.concurrent.TimeUnit

class NotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        showNotification()
        return Result.success()
    }

    // Create an Intent to open the app
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }

    // Wrap the Intent in a PendingIntent
    val pendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    private fun showNotification() {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Daily Reminder",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Channel for daily app usage reminders"
        }
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.palm_logo)
            .setContentTitle("Daily Reminder")
            .setContentText("Time to set your tasks for today!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val CHANNEL_ID = "daily_notification_channel"
        private const val NOTIFICATION_ID = 1

        fun scheduleDaily(context: Context) {
            val workManager = WorkManager.getInstance(context)
            val existingWorkInfo = workManager.getWorkInfosForUniqueWork("daily_notification_work")
                .get() // Note: This is a blocking call, should be used carefully

            if (existingWorkInfo.isEmpty() || existingWorkInfo.all { it.state.isFinished }){
                val currentTime = Calendar.getInstance()
                val targetTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 8)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)

                    if (before(currentTime)) {
                        add(Calendar.DAY_OF_YEAR, 1)
                    }
                }

                val initialDelay = targetTime.timeInMillis - currentTime.timeInMillis

                val dailyWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
                    24, TimeUnit.HOURS
                )
                    .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                            .build()
                    )
                    .build()

                WorkManager.getInstance(context)
                    .enqueueUniquePeriodicWork(
                        "daily_notification_work",
                        ExistingPeriodicWorkPolicy.REPLACE,
                        dailyWorkRequest
                    )
            }
        }

        fun cancelDaily(context: Context) {
            WorkManager.getInstance(context)
                .cancelUniqueWork("daily_notification_work")
        }
    }
}