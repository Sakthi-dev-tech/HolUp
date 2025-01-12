package com.adormantsakthi.holup.functions.statistics

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

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

        val lastDate = prefs.getLong(LAST_DATE, System.currentTimeMillis())
        val currentDate = System.currentTimeMillis()
        val millisecondsInADay = 24 * 60 * 60 * 1000L

        Log.d("Last Date", lastDate.toString())
        Log.d("Current Date", currentDate.toString())

        if (lastDate + millisecondsInADay < currentDate) {
            // New day
            prefs.edit().putLong(LAST_DATE, lastDate + millisecondsInADay).apply()
            prefs.edit().putInt(NUMBER_OF_TIMES, 0).apply()
        }

        return prefs.getInt(NUMBER_OF_TIMES, 0)
    }
}
