package com.adormantsakthi.holup.functions.statistics

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.util.Calendar

object NumOfTimesLimitedAppsAccessedStorage {
    private const val PREFS_NAME = "num_of_times_limited_apps_accessed"
    private const val NUMBER_OF_TIMES = "number_of_times"
    private const val LAST_DATE = "last_date"

    fun appAccessed(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        refreshCounter(context)

        prefs.edit().putInt(NUMBER_OF_TIMES, prefs.getInt(NUMBER_OF_TIMES, 0) + 1).apply()
    }

    fun refreshCounter(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val calendar = Calendar.getInstance()

        // Set the time to today at 12 AM
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val todayAtMidnightInMillis = calendar.timeInMillis

        // Retrieve the last recorded date
        val lastDate = prefs.getLong(LAST_DATE, 0)

        if (lastDate < todayAtMidnightInMillis) {
            // New day: reset the counter and update the last date
            prefs.edit()
                .putLong(LAST_DATE, todayAtMidnightInMillis)
                .putInt(NUMBER_OF_TIMES, 0)
                .apply()
        }

        return prefs.getInt(NUMBER_OF_TIMES, 0)
    }
}
