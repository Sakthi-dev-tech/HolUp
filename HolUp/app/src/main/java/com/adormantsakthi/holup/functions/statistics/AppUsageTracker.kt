package com.adormantsakthi.holup.functions.statistics

import android.app.usage.UsageStatsManager
import android.content.Context
import java.util.Calendar

class AppUsageTracker(private val context: Context) {
    private val timeRangeHelper = TimeRangeHelper()
    private val usageStatsHelper = AppUsageStatsHelper(context)

    val thisWeekUsageStats: MutableList<Float> = mutableListOf()
    val thisMonthUsageStats: MutableList<Float> = mutableListOf()

    /**
     * Gets usage statistics for each day of this week
     * @param packageNames List of package names to track
     * @return List of pairs containing the date timestamp and usage hours for that day
     */
    fun getThisWeekUsageStats(packageNames: List<String>): MutableList<Float> {
        timeRangeHelper.getThisWeekDailyRanges().map { (startTime, endTime) ->
            thisWeekUsageStats.add(usageStatsHelper.getDailyUsageStats(startTime, endTime, packageNames))
        }

        return thisWeekUsageStats
    }

    /**
     * Gets usage statistics for each day of last week
     * @param packageNames List of package names to track
     * @return List of pairs containing the date timestamp and usage hours for that day
     */
    fun getLastWeekUsageStats(packageNames: List<String>): List<Float> {
        return timeRangeHelper.getLastWeekDailyRanges().map { (startTime, endTime) ->
            usageStatsHelper.getDailyUsageStats(startTime, endTime, packageNames)
        }
    }

    /**
     * Gets usage statistics for each day of this month
     * @param packageNames List of package names to track
     * @return List of pairs containing the date timestamp and usage hours for that day
     */
    fun getThisMonthUsageStats(packageNames: List<String>): MutableList<Float> {
        timeRangeHelper.getThisMonthDailyRanges().map { (startTime, endTime) ->
            thisMonthUsageStats.add(usageStatsHelper.getDailyUsageStats(startTime, endTime, packageNames))
        }

        return thisMonthUsageStats
    }

    /**
     * Gets today's usage statistics
     * @param packageNames List of package names to track
     * @return Usage hours for today
     */
    fun getTodayUsageStats(packageNames: List<String>): Float {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return usageStatsHelper.getDailyUsageStats(
            calendar.timeInMillis,
            System.currentTimeMillis(),
            packageNames
        )
    }
}