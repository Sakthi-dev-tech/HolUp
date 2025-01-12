package com.adormantsakthi.holup.functions

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*
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

    private fun showNotification() {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Daily Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for daily app usage reminders"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) /** This is for the notification icon (CHANGE LATER!!!) */
            .setContentTitle("Daily Reminder")
            .setContentText("Time to set your tasks for today!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        // Show the notification
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val CHANNEL_ID = "daily_notification_channel"
        private const val NOTIFICATION_ID = 1

        fun scheduleDaily(context: Context) {
            val currentTime = Calendar.getInstance()
            val targetTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 8) // Target time: 8 AM
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)

                if (before(currentTime)) {
                    // If 8 AM today has already passed, schedule for tomorrow
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }

            val initialDelay = targetTime.timeInMillis - currentTime.timeInMillis

            // Schedule periodic work with an initial delay and repeat interval of 24 hours
            val dailyWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
                24, TimeUnit.HOURS
            )
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    "daily_notification_work",
                    ExistingPeriodicWorkPolicy.REPLACE, // Replace any existing work
                    dailyWorkRequest
                )
        }

        fun cancelDaily(context: Context) {
            WorkManager.getInstance(context)
                .cancelUniqueWork("daily_notification_work")
        }
    }
}
